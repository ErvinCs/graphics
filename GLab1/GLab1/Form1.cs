using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace GLab1
{
    public partial class Form1 : Form
    {
        private ViewPort viewPort;
        private Window window;

        private Rectangle currentPoint;
        private bool isLineDrawn;

        public Form1()
        {
            viewPort = new ViewPort(100, 100, 700, 500);
            window = new Window(101, 101, 699, 499, viewPort);

            currentPoint = new Rectangle();
            isLineDrawn = false;

            InitializeComponent();
        }

        private double Min3(double a, double b, double c)
        {
            return Math.Min(Math.Min(a, b), c);
        }

        private double Max3(double a, double b, double c)
        {
            return Math.Max(Math.Max(a, b), c);
        }

        private void Form1_MouseClick_1(object sender, MouseEventArgs e)
        {
            // Draw Point
            Graphics graphics = this.CreateGraphics();

            if (!(currentPoint.Equals(new Rectangle())))
            {
                Pen clearPen = new Pen(this.BackColor);
                graphics.DrawEllipse(clearPen, currentPoint);
            }

            Point clicked = new Point(e.X, e.Y);
            Console.WriteLine(clicked.ToString());
            if (!window.ContainsPoint(clicked))
                return;


            double x = window.U_1(clicked.X);
            double y = window.V_1(clicked.Y);

            Pen pen = new Pen(Color.Red, 1);
            currentPoint = new Rectangle(window.U(x), window.V(y), 4, 4);   // -2 -2
            graphics.DrawEllipse(pen, currentPoint);

            // Update Labels
            label1.Text = "X = " + currentPoint.X;
            label2.Text = "Y = " + currentPoint.Y;

            // Draw Distance to Line
            if (isLineDrawn)
            {
                Point p1 = new Point(150, 150);
                Point p2 = new Point(400, 400);
                Point closest = Distance(new Point(window.U(x), window.V(y)), p1, p2);
                graphics.DrawLine(pen, window.U(x), window.V(y), closest.X, closest.Y);
            }
        }

        private Point Distance(Point pt, Point p1, Point p2)
        {
            float dx = p2.X - p1.X;
            float dy = p2.Y - p1.Y;

            // Find the minimum distance
            float t = ((pt.X - p1.X) * dx + (pt.Y - p1.Y) * dy) /
                (dx * dx + dy * dy);

            // Segment ends
            if (t < 0)
            {
                dx = pt.X - p1.X;
                dy = pt.Y - p1.Y;
                return new Point(p1.X, p1.Y);
            }
            else if (t > 1)
            {
                dx = pt.X - p2.X;
                dy = pt.Y - p2.Y;
                return new Point(p2.X, p2.Y);
            }
            else
            {
                Point output = new Point((int)(p1.X + t * dx),(int)(p1.Y + t * dy));
                dx = pt.X - output.X;
                dy = pt.Y - output.Y;
                return output;
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Graphics graphics = this.CreateGraphics();

            // TODO - Erase old line

            // T0D0 - Random Line
            // Random random = new Random();
            Point p1 = new Point(150, 150);
            Point p2 = new Point(400, 400);
            Pen pen = new Pen(Color.DarkSlateBlue, 2);
            graphics.DrawLine(pen, p1.X, p1.Y, p2.X, p2.Y);
            isLineDrawn = true;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Graphics graphics = this.CreateGraphics();
            Pen pen = new Pen(Color.Black);

            //
            //Pen fPen = new Pen(Color.Cyan);
            //PointF[] pointFs = new PointF[801];
            //pointFs[0] = new PointF(0, (int)(Math.Sin(0) * 100));
            //

            Point previous = new Point(0, (int)(Math.Sin(0) * 100));
            for(int x = 1; x <= 800; x++)
            {
                Point current = new Point(x, (int)(Math.Sin(x) * 100));
                Console.WriteLine("Sine(" + x + "): " + current.ToString() + " | Sin(x)=" + Math.Sin(x));

                //double Xc = window.U_1(current.X);
                //double Yc = window.V_1(current.Y);

                //double Xp = window.U_1(previous.X);
                //double Yp = window.V_1(previous.Y);


                graphics.DrawLine(pen, previous.X, previous.Y, current.X, current.Y);
                //graphics.DrawLine(pen, window.U(Xc), window.V(Yc), window.U(Xp), window.V(Yp));
                previous = current;

                //
                //pointFs[x] = new PointF(x, (int)(Math.Sin(x) * 100));
                //
            }
            //
            //graphics.DrawCurve(fPen, pointFs);
            //
        }
    }


}
