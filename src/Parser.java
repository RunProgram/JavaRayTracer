import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    String fileName;

    public Parser(String fileName) {
        this.fileName = fileName;
    }

    public Group composeShape(Material material, boolean isSmoothed) {
        Group gShape = new Group(); // a group of parts
        Group gPart = new Group(); // a part of triangles

        ArrayList<RTuple> vList = new ArrayList<>(); // vertices
        ArrayList<RTuple> vnList = new ArrayList<>(); // vertex normals
        ArrayList<RTuple> fPointList = new ArrayList<>(); // face (points)
        ArrayList<RTuple> fNormalList = new ArrayList<>(); // face (normals)
        boolean normalsAvailable = false;

        RTuple p1, p2, p3, n1, n2, n3;

        try {
            File OBJShape = new File(fileName);
            Scanner myReader = new Scanner(OBJShape);

            boolean firstPart = true;
            String partName = "defaultName";

            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();

                // Remove extra spaces
                int iEnd = currentLine.length() - 1;
                while (iEnd >= 0 && currentLine.charAt(iEnd) == ' ') iEnd--;
                currentLine = currentLine.substring(0, iEnd + 1); // remove trailing spaces
                currentLine = currentLine.replaceAll("  ", " "); // remove double spaces

                // Detect named parts
                if (currentLine.length() > 1 && currentLine.substring(0, 2).equals("g ")) {
                    if (!firstPart) {
                        gPart.setName(partName);
                        for (int i = 0; i < fPointList.size(); i++) {
                            p1 = vList.get((int) fPointList.get(i).x - 1);
                            p2 = vList.get((int) fPointList.get(i).y - 1);
                            p3 = vList.get((int) fPointList.get(i).z - 1);

                            if (isSmoothed) {
                                n1 = vnList.get((int) fNormalList.get(i).x - 1);
                                n2 = vnList.get((int) fNormalList.get(i).y - 1);
                                n3 = vnList.get((int) fNormalList.get(i).z - 1);
                                TriangleS triangle = new TriangleS(material, p1, p2, p3, n1, n2, n3);
                                gPart.addPart(triangle); // make the final part
                            } else {
                                Triangle triangle = new Triangle(material, p1, p2, p3);
                                gPart.addPart(triangle); // make the final part
                            }
                        }
                        gShape.addPart(gPart); // add the part to the shape
                        // Reset for a new part
                        partName = currentLine.substring(2); // to the end
                        gPart = new Group();
                        fPointList.clear();
                        fNormalList.clear();
                    } else partName = currentLine.substring(2); // to the end
                }

                // Collect all vertices
                if (currentLine.length() > 1 && currentLine.substring(0, 2).equals("v ")) {
                    String readyLine = currentLine.replaceAll("v ", "");
                    String[] sNums = readyLine.split(" ");
                    if (sNums.length == 3) {
                        double[] dNums = new double[3];
                        for (int i = 0; i < 3; i++) dNums[i] = Double.parseDouble(sNums[i]);
                        vList.add(new RTuple(dNums[0], dNums[1], dNums[2], 1));
                    } else {
                        System.out.println("Vertices: Three coordinates are required!");
                    }
                }

                // Collect all vertex normals (only if the shape is smoothed)
                if (isSmoothed) {
                    if (currentLine.length() > 1 && currentLine.substring(0, 3).equals("vn ")) {
                        String readyLine = currentLine.replaceAll("vn ", "");
                        String[] sNums = readyLine.split(" ");
                        if (sNums.length == 3) {
                            double[] dNums = new double[3];
                            for (int i = 0; i < 3; i++) dNums[i] = Double.parseDouble(sNums[i]);
                            vnList.add(new RTuple(dNums[0], dNums[1], dNums[2], 0));
                        } else {
                            System.out.println("Vertex normals: Three coordinates are required!");
                        }
                    }
                }

                if (currentLine.length() > 1 && currentLine.substring(0, 2).equals("f ")) {
                    if (firstPart) firstPart = false;
                    String readyLine = currentLine.replaceAll("f ", "");
                    String[] sNums = readyLine.split(" ");
                    int howMany = sNums.length;
                    int[] iPoints = new int[howMany];
                    int[] iNormals = new int[howMany];
                    for (int i = 0; i < howMany; i++) {
                        String[] sNumsSplit = sNums[i].split("/");
                        iPoints[i] = Integer.parseInt(sNumsSplit[0]); // the first number (vertex index)
                        if (isSmoothed)
                            iNormals[i] = Integer.parseInt(sNumsSplit[2]); // the first number (vertex index)
                    }
                    // Split polygons into triangles
                    for (int i = 0; i < howMany - 2; i++) {
                        fPointList.add(new RTuple(iPoints[0], iPoints[i + 1], iPoints[i + 2], 1));
                        if (isSmoothed) fNormalList.add(new RTuple(iNormals[0], iNormals[i + 1], iNormals[i + 2], 1));
                    }
                }
            }

            if (isSmoothed && (fPointList.size() != fNormalList.size())) {
                System.out.print("The point and normal sets don't match: Disregarding smoothing!");
                isSmoothed = false;
            }

            gPart.setName(partName);
            for (int i = 0; i < fPointList.size(); i++) {
                p1 = vList.get((int) fPointList.get(i).x - 1);
                p2 = vList.get((int) fPointList.get(i).y - 1);
                p3 = vList.get((int) fPointList.get(i).z - 1);

                if (isSmoothed) {
                    n1 = vnList.get((int) fNormalList.get(i).x - 1);
                    n2 = vnList.get((int) fNormalList.get(i).y - 1);
                    n3 = vnList.get((int) fNormalList.get(i).z - 1);
                    TriangleS triangle = new TriangleS(material, p1, p2, p3, n1, n2, n3);
                    gPart.addPart(triangle); // make the smoothed part
                } else {
                    Triangle triangle = new Triangle(material, p1, p2, p3);
                    gPart.addPart(triangle); // make the non-smoothed part
                }
            }
            gShape.addPart(gPart); // add the part to the shape

            System.out.println("Part List:");
            for (int s = 0; s < gShape.shapeList.size(); s++) {
                Shape currentShape = gShape.shapeList.get(s);
                System.out.println(currentShape.getName());
            }

            return gShape;
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found.");
            e.printStackTrace();
            return null;
        }
    }
}