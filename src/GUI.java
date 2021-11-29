import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


// This class inherits properties from JPanel (its superclass).
// It also implements two interfaces: ActionListener (for the menu) and Runnable (for threads).
public class GUI extends JPanel implements ActionListener {
    // INSTANCE VARIABLES

    final int windowXMax = 1024; // computer window
    final int windowYMax = 1024; // computer window

    RColor[][] colorArray = new RColor[windowYMax][windowXMax];

    int projectID;

    RColor currLightColor;

    JFrame f; // frame

    JMenuBar mb; // menu bar

    JMenu file; // option in the menu bar
    JMenu light; // option in the menu bar
    JMenu canvas; // option in the menu bar
    JMenu motion; //option in the menu bar

    JMenuItem open; // menu action item
    JMenuItem save; // menu action item
    JMenuItem whiteLight; // menu action item
    JMenuItem redLight; // menu action item
    JMenuItem yellowLight; // menu action item
    JMenuItem greenLight; // menu action item
    JMenuItem blueLight; // menu action item
    JMenuItem project1;
    JMenuItem project2;
    JMenuItem project3;
    JMenuItem project4;
    JMenuItem project5;
    JMenu project6;
    JMenuItem project6A;
    JMenuItem project6B;
    JMenuItem project6C;
    JMenuItem clear; // menu action item
    JMenuItem pan;
    JMenuItem stop;


    // CONSTRUCTOR
    GUI() {

        currLightColor = new RColor(1, 1, 1);

        projectID = 1;

        for (int j = 0; j < windowYMax; j++)
            for (int i = 0; i < windowXMax; i++)
                colorArray[j][i] = new RColor(0, 0, 0);

        f = new JFrame("Ray Tracer"); // create a frame
        f.setIconImage(Toolkit.getDefaultToolkit().getImage("rt.png")); // OPTIONAL: use your own icon (stored in the same folder)
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // hide on closing, to allow the thread to stop (EXIT_ON_CLOSE otherwise)
        setPreferredSize(new Dimension(windowXMax, windowYMax)); // window size
        f.add(this); // add the panel to the frame (remember: "this" class is an "extended" panel!)
        f.setBackground(new Color(0, 0, 0)); // add background

        f.setResizable(false); // OPTIONAL: allow or prevent manual resizing

// Create menu action items (inside dropdown menus).
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        whiteLight = new JMenuItem("White");
        redLight = new JMenuItem("Red");
        greenLight = new JMenuItem("Green");
        yellowLight = new JMenuItem("Yellow");
        blueLight = new JMenuItem("Blue");
        project1 = new JMenuItem("Project 1");
        project2 = new JMenuItem("Project 2");
        project3 = new JMenuItem("Project 3");
        project4 = new JMenuItem("Project 4");
        project5 = new JMenuItem("Project 5");
        project6A = new JMenuItem("Low-resolution Non-smoothed");
        project6B = new JMenuItem("Low-resolution Smoothed");
        project6C = new JMenuItem("High-resolution Smoothed");
        clear = new JMenuItem("Clear");
        pan = new JMenuItem("Pan");
        stop = new JMenuItem("Stop");

// Make action items produce an "event" when selected.
        open.addActionListener(this);
        save.addActionListener(this);
        whiteLight.addActionListener(this);
        redLight.addActionListener(this);
        greenLight.addActionListener(this);
        yellowLight.addActionListener(this);
        blueLight.addActionListener(this);
        project1.addActionListener(this);
        project2.addActionListener(this);
        project3.addActionListener(this);
        project4.addActionListener(this);
        project5.addActionListener(this);
        project6A.addActionListener(this);
        project6B.addActionListener(this);
        project6C.addActionListener(this);
        clear.addActionListener(this);
        pan.addActionListener(this);
        stop.addActionListener(this);

// Add options to the menu bar.
        file = new JMenu("File");
        light = new JMenu("Light");
        canvas = new JMenu("Canvas");
        motion = new JMenu("Motion");
        project6 = new JMenu("Complex Teapot");

// Bind action items to an option in the menu bar (size).
        file.add(open);
        file.add(save);
        light.add(whiteLight);
        light.add(redLight);
        light.add(greenLight);
        light.add(yellowLight);
        light.add(blueLight);
        canvas.add(project1);
        canvas.add(project2);
        canvas.add(project3);
        canvas.add(project4);
        canvas.add(project5);
        canvas.add(project6);
        canvas.add(clear);
        motion.add(pan);
        motion.add(stop);

// Add all menu options to the menu bar.
        mb = new JMenuBar();
        mb.add(file);
        mb.add(light);
        mb.add(canvas);
        mb.add(motion);
        project6.add(project6A);
        project6.add(project6B);
        project6.add(project6C);

// Add the menu bar to the frame.
        f.setJMenuBar(mb);

// Adjust the frame to contain the full panel (after the menu!).
        f.pack();

// Make the frame visible.
        f.setVisible(true);
    }

    // paintComponent is a protected method in JPanel and in its parent, JComponent.
// It is the only method that actually draws. The other methods call it with repaint() or by default.
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; // Graphics2D extends Graphics; needed to change line width (stroke)

        g2.setStroke(new BasicStroke(1));
        int red, green, blue;

        for (int j = 0; j < windowYMax; j++) {
            for (int i = 0; i < windowXMax; i++) {
                red = (int) (colorArray[j][i].r * 255);
                green = (int) (colorArray[j][i].g * 255);
                blue = (int) (colorArray[j][i].b * 255);
                g2.setColor(new Color(red, green, blue));
                g2.drawLine(i, j, i, j);
            }
        }
    }

    public void render(World w, Camera cam, int remaining){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = 0; j < windowYMax/4; j++) {
                    for (int i = 0; i < windowXMax/4; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = windowYMax/4; j < windowYMax/2; j++) {
                    for (int i = 0; i < windowXMax/4; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = windowYMax/2; j < (windowYMax/4)*3; j++) {
                    for (int i = 0; i < windowXMax/4; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = (windowYMax/4)*3; j < windowYMax; j++) {
                    for (int i = 0; i < windowXMax/4; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = 0; j < windowYMax/4; j++) {
                    for (int i = windowXMax/4; i < windowXMax/2; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t6 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = windowYMax/4; j < windowYMax/2; j++) {
                    for (int i = windowXMax/4; i < windowXMax/2; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t7 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = windowYMax/2; j < (windowYMax/4)*3; j++) {
                    for (int i = windowXMax/4; i < windowXMax/2; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t8 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = (windowYMax/4)*3; j < windowYMax; j++) {
                    for (int i = windowXMax/4; i < windowXMax/2; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t9 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = 0; j < windowYMax/4; j++) {
                    for (int i = windowXMax/2; i < (windowXMax/4)*3; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t10 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = windowYMax/4; j < windowYMax/2; j++) {
                    for (int i = windowXMax/2; i < (windowXMax/4)*3; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t11 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = windowYMax/2; j < (windowYMax/4)*3; j++) {
                    for (int i = windowXMax/2; i < (windowXMax/4)*3; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t12 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = (windowYMax/4)*3; j < windowYMax; j++) {
                    for (int i = windowXMax/2; i < (windowXMax/4)*3; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t13 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = 0; j < windowYMax/4; j++) {
                    for (int i = (windowXMax/4)*3; i < windowXMax; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t14 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                for (int j = windowYMax/4; j < windowYMax/2; j++) {
                    for (int i = (windowXMax/4)*3; i < windowXMax; i++) {
                        Ray r6;
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t15 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = windowYMax/2; j < (windowYMax/4)*3; j++) {
                    for (int i = (windowXMax/4)*3; i < windowXMax; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        Thread t16 = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 4;
                Ray r6;
                for (int j = (windowYMax/4)*3; j < 1024; j++) {
                    for (int i = (windowXMax/4)*3; i < windowXMax; i++) {
                        r6 = RT.rayForPixel(cam, i, j);
                        colorArray[j][i] = RT.colorAt(w, r6, remaining);
                    }
                }
                repaint();
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();
        t11.start();
        t12.start();
        t13.start();
        t14.start();
        t15.start();
        t16.start();
    }

    // MENU CONTROL
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == save) {
            try(ObjectOutputStream fout = new ObjectOutputStream(new FileOutputStream("raytracer.rt", false))){
                fout.writeObject(colorArray);
            }
            catch(IOException exc){
                System.out.println("IOException occurred when trying to save.");
                exc.printStackTrace();
            }

        }

        if (e.getSource() == open) {
            try(ObjectInputStream fin = new ObjectInputStream(new FileInputStream("raytracer.rt"))){
                colorArray = (RColor[][]) fin.readObject();
                repaint();
            }
            catch(IOException i)
            {
                i.printStackTrace();
                System.out.println("Error reading file!");
            }
            catch(ClassNotFoundException c) {
                System.out.println("Class not found!");
            }
        }

        if(e.getSource() == clear){
            for (int j = 0; j < windowYMax; j++){
                for (int i = 0; i < windowXMax; i++){
                    colorArray[j][i] = new RColor(0, 0, 0);
                }
            }
            repaint();
        }

        if (e.getSource() == whiteLight) {
            currLightColor = new RColor(1, 1, 1);
        }

        if (e.getSource() == redLight) {
            currLightColor = new RColor(1, 0, 0);
        }

        if (e.getSource() == yellowLight) {
            currLightColor = new RColor(1, 1, 0);
        }

        if (e.getSource() == greenLight) {
            currLightColor = new RColor(0, 1, 0);
        }

        if (e.getSource() == blueLight) {
            currLightColor = new RColor(0, 0, 1);
        }



        if (e.getSource() == project1) {
            long startTime = System.currentTimeMillis();
            projectID = 2;

            World w = new World(new PointLight(new RTuple(50, 100, -50, 1), currLightColor));
            Camera cam = new Camera(windowXMax, windowYMax, Math.PI / 3);
            w.setView(new RTuple(0, 0, -5, 1), new RTuple(0, 0, 0, 1), new RTuple(0, 1, 0, 0));
            cam.setTransform(w.viewTransform);

            Material wMat = new Material(new RColor(1, 1, 1), 0.1, 0.7, 0, 0., 0.1, 0, 0);
            Material bMat = new Material(new RColor(0.537, 0.831, 0.914), 0.1, 0.7, 0, 0, 0.1, 0, 0);
            Material rMat = new Material(new RColor(0.941, 0.322, 0.388), 0.1, 0.7, 0, 0, 0.1, 0, 0);
            Material pMat = new Material(new RColor(0.373, 0.404, 0.55), 0.1, 0.7, 0, 0, 0.1, 0, 0);

            Plane bg = new Plane(new Material(new RColor(1, 1, 1), 1, 0, 0, 0, 0, 0, 0));
            bg.transform("x", Math.PI/2);
            bg.transform("translate", 0, 0, 500);

            Sphere mainS = new Sphere(new Material(new RColor(0, 0, 0),0.1, 0.7, 0.3, 200,0, 1, 1.52));
            //mainS.transform("translate", 1, -1, 1);

            Cube c1 = new Cube(wMat);
            c1.transform("translate", 4, 0, 0);
            c1.transform("scale", 3, 3, 3);

            w.addThing(bg);
            w.addThing(mainS);
            w.addThing(c1);

            int remaining = 4;
            render(w, cam, remaining);

            long endTime = System.currentTimeMillis();
            System.out.println("Rendering time: " + (endTime - startTime) / 1000. + " seconds.");
        }
        if (e.getSource() == project2) {
            long startTime = System.currentTimeMillis();
            PatternCheckers pFloor = new PatternCheckers(new RColor(0.2, 0.2, 0.2), new RColor(1, 1, 1));
            Material mFloor = new Material(0.1, 0.9, 0.9, 300, 0, 0, 1, pFloor);
            Plane Floor = new Plane(mFloor);
            Floor.transform("translate", 0, -2, 0);

            Material mWall = new Material(new RColor(1, 1, 1), 0.1, 0.9, 0.9, 300, 0, 0, 1);

            Plane Wall1 = new Plane(mWall);
            Wall1.transform("x", Math.PI / 2);
            Wall1.transform("translate", 0, 0, 20);

            Plane Wall2 = new Plane(mWall);
            Wall2.transform("z", Math.PI / 2);
            Wall2.transform("translate", -12, 0, 0);

            Plane Wall3 = new Plane(mWall);
            Wall3.transform("z", Math.PI / 2);
            Wall3.transform("translate", 12, 0, 0);

            Material mCeiling = new Material(0.1, 0.9, 0.9, 300, 0, 0, 1, pFloor);
            Plane Ceiling = new Plane(mCeiling);
            Ceiling.transform("translate", 0, 3, 0);

            Material mColumn = new Material(new RColor(0.75, 0.75, 0.75), 0.1, 0.9, 0.9, 300, 0.8, 0, 1);
            Cylinder Column1 = new Cylinder(mColumn);
            Column1.transform("scale", 0.5, 0.5, 0.5);
            Column1.transform("translate", -4, 0, 4);
            Cylinder Column2 = new Cylinder(mColumn);
            Column2.transform("scale", 0.5, 0.5, 0.5);
            Column2.transform("translate", 4, 0, 4);


            Material mBall1 = new Material(new RColor(0.8, 0, 0.1), 0.1, 0.2, 0.9, 300, 0.2, 1, 1.52);
            Sphere Ball1 = new Sphere(mBall1);
            Ball1.transform("scale", 1, 1, 1);
            Ball1.transform("translate", -2, -1, 3);

            Material mBall2 = new Material(new RColor(0.2, 0.8, 0.2), 0.1, 0.2, 0.9, 300, 0.8, 0, 1);
            Sphere Ball2 = new Sphere(mBall2);
            Ball2.transform("scale", 1, 1, 1);
            Ball2.transform("translate", 5.5, -1, 10);

            Material mSandglass = new Material(new RColor(0.75, 0.75, 0.75), 0.1, 0.9, 0.9, 300, 0.8, 0, 1);
            Cone Sandglass = new Cone(mSandglass, -1, 1, true);
            Sandglass.transform("scale", 0.5, 0.5, 0.5);
            Sandglass.transform("translate", 1.1, -0.8, 4);


            Material mBlock = new Material(new RColor(0.1, 0.1, 0.7), 0.1, 0.2, 0.9, 300, 0.8, 0, 1);
            Cube Block = new Cube(mBlock);
            Block.transform("scale", 1.6, 0.4, 1.6);
            Block.transform("translate", 1, -1.8, 4);

            World w = new World(new PointLight(new RTuple(0, 0, 15, 1), currLightColor));
            w.addThing(Floor);
            w.addThing(Wall1);
            w.addThing(Wall2);
            w.addThing(Wall3);
            w.addThing(Ceiling);
            w.addThing(Column1);
            w.addThing(Column2);
            w.addThing(Ball1);
            w.addThing(Ball2);
            w.addThing(Sandglass);
            w.addThing(Block);

            Camera cam = new Camera(windowXMax, windowYMax, Math.PI / 3);
            w.setView(new RTuple(0, 0, -5, 1), new RTuple(0, 0, 0, 1), new RTuple(0, 1, 0, 0));
            cam.setTransform(w.viewTransform);

            int remaining = 4;
            render(w, cam, remaining);
            long endTime = System.currentTimeMillis();
            System.out.println("Rendering time: " + (endTime - startTime) / 1000. + " seconds.");
        }

        if (e.getSource() == project3) {
            PatternCheckers pFloor = new PatternCheckers(new RColor(0.9, 0.3, 0.2), new RColor(0.3, 0.3, 0.8));
            pFloor.transform("scale", 0.4, 0.4, 0.4);
            Material mFloor = new Material(0.1, 0.9, 0, 200, 0, 0, 1, pFloor);
            Plane floor = new Plane(mFloor);

            PatternRings pWall = new PatternRings(new RColor(0, 0, 0), new RColor(0.2, 0.2, 0.2));
            pWall.transform("scale", 0.6, 0.6, 0.6);
            Material mWall = new Material(0.1, 0.9, 0, 200, 0, 0, 1, pWall);
            Plane wall = new Plane(mWall);
            wall.transform("x", Math.PI / 2);
            wall.transform("translate", 0, 0, 20);

            PatternStripes pBall1 = new PatternStripes(new RColor(1, 0.5, 1), new RColor(1, 1, 0.5));
            pBall1.transform("scale", 0.05, 0.1, 0.1);
            pBall1.transform("z", Math.PI / 2);
            //Material mBall1 = new Material(0.1,0.7,0.3,200, 0, pBall1);
            Material mBall1 = new Material(new RColor(0.2, 0.5, 0.7), 0.1, 0.7, 0.3, 200, 0.5, 0, 1);
            Sphere ball1 = new Sphere(mBall1);
            ball1.transform("translate", -0.5, 1, 1);

            PatternRings pBall2 = new PatternRings(new RColor(1, 0.84, 0), new RColor(0.6, 0.6, 0.6));
            pBall2.transform("scale", 0.05, 0.1, 0.1);
            Material mBall2 = new Material(0.1, 0.7, 0.3, 200, 0, 0, 1, pBall2);
            Sphere ball2 = new Sphere(mBall2);
            ball2.transform("scale", 0.6, 0.2, 0.6);
            ball2.transform("translate", -1.9, 0.6, 0);

            PatternGradient pBall3 = new PatternGradient(new RColor(1, 0, 0), new RColor(0, 1, 0));
            pBall3.transform("scale", 0.5, 0.5, 0.5);
            Material mBall3 = new Material(0.1, 0.7, 0.3, 200, 0, 0, 1, pBall3);
            Sphere ball3 = new Sphere(mBall3);
            ball3.transform("scale", 0.7, 0.7, 0.7);
            ball3.transform("translate", 3, 0.7, 2);

            Material mBall4 = new Material(new RColor(0, 0, 0), 0.1, 0.7, 0.3, 200, 0, 1, 1.52);

            Sphere ball4 = new Sphere(mBall4);
            ball4.transform("scale", 0.5, 0.5, 0.5);
            ball4.transform("translate", 1, 1.3, 0);

            PatternCheckers pCube = new PatternCheckers(new RColor(0.5, 1, 0.2), new RColor(0.2, 0.4, 0.8));
            pCube.transform("scale", 0.1, 0.1, 0.1);
            Material mCube = new Material(0.1, 0.7, 0.3, 200, 0, 0, 1, pCube);
            Cube cube = new Cube(mCube);
            cube.transform("scale", 0.4, 0.4, 0.4);
            cube.transform("translate", 1, 0.4, 0);

            Parser p = new Parser("icosphere.obj");

            World w = new World(new PointLight(new RTuple(5, 1, -1, 1), currLightColor));

            /*for(int r = 0; r < p.f.length; r++){
                int i1 = (int) p.f[r][0];
                int i2 = (int) p.f[r][1];
                int i3 = (int) p.f[r][2];

                RTuple p1 = new RTuple(p.v[i1][0], p.v[i1][1], p.v[i1][2], 1);
                RTuple p2 = new RTuple(p.v[i2][0], p.v[i2][1], p.v[i2][2], 1);
                RTuple p3 = new RTuple(p.v[i3][0], p.v[i3][1], p.v[i3][2], 1);

                Triangle t = new Triangle(p1, p2, p3);
                w.addThing(t);
            }*/

            //World w = new World(new PointLight(new RTuple(-10, 10, -10, 1), currLightColor));
            w.addThing(floor);
            w.addThing(wall);
            w.addThing(ball1);
            w.addThing(ball2);
            w.addThing(ball3);
            w.addThing(ball4);
            w.addThing(cube);

            Camera cam = new Camera(windowXMax, windowYMax, Math.PI / 3);
            w.setView(new RTuple(0, 1.5, -5, 1), new RTuple(0, 1, 0, 1), new RTuple(0, 1, 0, 0));
            cam.setTransform(w.viewTransform);

            int remaining = 4;
            render(w, cam, remaining);
        }

        if (e.getSource() == project4) {
            long startTime = System.currentTimeMillis();

            World w = new World(new PointLight(new RTuple(-3, 2.5, -3, 1), currLightColor));

            Parser p = new Parser("dog.obj");

            Material m = new Material(new RColor(0.5882, 0.2941, 0));

            Group g = p.composeShape(m, false);
            g.transform("z", Math.PI);
            g.transform("x", Math.PI/3);


            //g.transform("x", -Math.PI / 2);

            //g.transform("transform", 0, 3, 0);

            g.transform("scale", 0.01, 0.01, 0.01);

            PatternCheckers myPattern = new PatternCheckers(new RColor(0.8, 0.8, 0.8), new RColor(0.9, 0.9, 0.9));
            Material myMaterial = new Material(0.1, 0.9, 0.9, 300, 0, 0, 1, myPattern);

            Plane Floor = new Plane(myMaterial);
            Floor.transform("translate", 0, -2, 0);

            Plane Wall1 = new Plane(myMaterial);
            Wall1.transform("x", Math.PI / 2);
            Wall1.transform("translate", 0, 0, 20);

            Plane Wall2 = new Plane(myMaterial);
            Wall2.transform("z", Math.PI / 2);
            Wall2.transform("translate", -12, 0, 0);

            Plane Wall3 = new Plane(myMaterial);
            Wall3.transform("z", Math.PI / 2);
            Wall3.transform("translate", 12, 0, 0);

            w.addThing(g);
            w.addThing(Floor);
            w.addThing(Wall1);
            w.addThing(Wall2);
            w.addThing(Wall3);

            Camera cam = new Camera(windowXMax, windowYMax, Math.PI / 3);
            w.setView(new RTuple(0, 2, -5, 1), new RTuple(0, 0, 0, 1), new RTuple(0, 1, 0, 0));
            cam.setTransform(w.viewTransform);

            int remaining = 4;
            render(w, cam, remaining);

            long endTime = System.currentTimeMillis();
            System.out.println("Rendering time: " + (endTime - startTime) / 1000. + " seconds");

        }

        if(e.getSource() == project5){
            Material mFloor = new Material(new RColor(1, 0, 0), 0.1, 0.9, 0.9, 300, 0, 0, 1);
            Plane Floor = new Plane(mFloor);
            Floor.transform("translate", 0, -2, 0);

            Group Hex = hexagon();
            Hex.transform("x", Math.PI / 2);
            Hex.transform("translate", 0, 0, 1);

            World w = new World(new PointLight(new RTuple(-4, 4, -20, 1), currLightColor));
            w.addThing(Floor);
            w.addThing(Hex);

            Camera cam = new Camera(windowXMax, windowYMax, Math.PI / 3);
            w.setView(new RTuple(0, 0, -5, 1), new RTuple(0, 0, 0, 1), new RTuple(0, 1, 0, 0));
            cam.setTransform(w.viewTransform);

            int remaining = 4;
            render(w, cam, remaining);
        }

        int whichOne = 0;
        Parser p = null;

        Group g = null;
        if (e.getSource() == project6A) whichOne = 1;
        if (e.getSource() == project6B) whichOne = 2;
        if (e.getSource() == project6C) whichOne = 3;

        if (whichOne > 0) {
            long startTime = System.currentTimeMillis();
            if (whichOne == 1 || whichOne == 2) p = new Parser("smallTeapot.obj");
            if (whichOne == 3) p = new Parser("Teapot.obj");
            //Material m = new Material(new RColor(0.63, 0.32, 0.18), 0.1, 0.9, 0.9, 300, 0.2, 0, 1);
            Material m = new Material(new RColor(0, 0, 0),0.1, 0.7, 0.3, 200,0, 1, 1.52);

            if (whichOne == 1) g = p.composeShape(m, false);
            else g = p.composeShape(m, true);
            g.transform("scale", 0.15, 0.15, 0.15);
            g.transform("x", -Math.PI / 2);
            g.transform("translate", 0, -2, 4);

            World w6 = new World(new PointLight(new RTuple(-3, 2.5, 2, 1), currLightColor));
            //World w6 = new World(new PointLight(new RTuple(0, 2, 4, 1), currLightColor));
            Camera cam6 = new Camera(windowXMax, windowYMax, Math.PI / 3);

            PatternCheckers myPattern = new PatternCheckers(new RColor(0.8, 0.8, 0.8), new RColor(0.9, 0.9, 0.9));
            Material myMaterial = new Material(0.1, 0.9, 0.9, 300, 0, 0, 1, myPattern);

            Plane Floor = new Plane(myMaterial);
            Floor.transform("translate", 0, -2, 0);

            Plane Wall1 = new Plane(myMaterial);
            Wall1.transform("x", Math.PI / 2);
            Wall1.transform("translate", 0, 0, 20);

            Plane Wall2 = new Plane(myMaterial);
            Wall2.transform("z", Math.PI / 2);
            Wall2.transform("translate", -12, 0, 0);

            Plane Wall3 = new Plane(myMaterial);
            Wall3.transform("z", Math.PI / 2);
            Wall3.transform("translate", 12, 0, 0);

            w6.addThing(g);
            w6.addThing(Floor);
            w6.addThing(Wall1);
            w6.addThing(Wall2);
            w6.addThing(Wall3);

            w6.setView(new RTuple(0, 2, -5, 1), new RTuple(0, 0, 0, 1), new RTuple(0, 1, 0, 0));
            cam6.setTransform(w6.viewTransform);

            int remaining = 4;

            render(w6, cam6, remaining);

            /*int remaining = 4;
            Ray r6;
            for (int j = 0; j < windowYMax; j++) {
                for (int i = 0; i < windowXMax; i++) {
                    r6 = RT.rayForPixel(cam6, i, j);
                    colorArray[j][i] = RT.colorAt(w6, r6, remaining);
                }
            }
            repaint();*/

        }

    }

    Sphere hexagonCorner(){
        Material m = new Material(new RColor(0.8, 0.8, 1), 0.1, 0.9, 0.9, 300, 0.5, 0, 1);
        Sphere corner = new Sphere(m);
        corner.transform("scale", 0.25, 0.25, 0.25);
        corner.transform("translate", 0, 0, -1);
        return corner;
    }
    Cylinder hexagonEdge(){
        Material m = new Material(new RColor(0.8, 0.8, 1), 0.1, 0.9, 0.9, 300, 0.5, 0, 1);
        Cylinder cylinder = new Cylinder(m, 0, 1, false);
        cylinder.transform("scale", 0.25, 1, 0.25);
        cylinder.transform("z", -Math.PI / 2);
        cylinder.transform("y", -Math.PI / 6);
        cylinder.transform("translate", 0, 0, -1);
        return cylinder;
    }
    Group hexagonSide(){
        Group side = new Group();
        side.addPart(hexagonCorner());
        side.addPart(hexagonEdge());
        return side;
    }

    Group hexagon(){
        Group hex = new Group();
        for(int n = 0; n < 6; n++){
            Shape side = hexagonSide();
            side.transform("y", n * Math.PI / 3);
            hex.addPart(side);
        }
        return hex;
    }


}
