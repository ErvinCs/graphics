using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GLab1
{
    class Line
    {
        private Point a;
        private Point b;

        public Point A { get => a; set => a = value; }
        public Point B { get => b; set => b = value; }

        public Line(Point a, Point b)
        {
            this.a = a;
            this.b = b;
        }

        public float Slope()
        {
            return (a.Y - b.Y) / (a.X - b.X);
        }
    }
}
