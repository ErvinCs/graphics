using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GLab1
{
    class Window
    {
        private double a, b, c, d;
        private ViewPort viewPort;

        public double A { get => a; set => a = value; }
        public double B { get => b; set => b = value; }
        public double C { get => c; set => c = value; }
        public double D { get => d; set => d = value; }

        public Window(double x1, double y1, double x2, double y2, ViewPort viewPort)
        {
            this.a = x1;
            this.b = x2;
            this.c = y2;
            this.d = y1;
            this.viewPort = viewPort;
        }

        public int U(double x)
        {
            return (int)((x - a) / (b - a) * (viewPort.U2 - viewPort.U1) + viewPort.U1);
        }

        public int V(double y)
        {
            return (int)((y - d) / (c - d) * (viewPort.V2 - viewPort.V1) + viewPort.V1);
        }

        public double U_1(int u)
        {
            return (u - viewPort.U1) * (b - a) / (viewPort.U2 - viewPort.U1) + a;
        }

        public double V_1(int v)
        {
            return (v - viewPort.V1) * (c - d) / (viewPort.V2 - viewPort.V1) + d;
        }

        public bool ContainsPoint(Point point)
        {
            if (point.X < a || point.X > b || point.Y < d || point.Y > c )
                return false;
            return true;
        }

        public override string ToString()
        {
            return  "Window(x1=" + a + ", y1=" + b +
                    "x2=" + c + ", y2=" + d + ')';
        }
    }
}
