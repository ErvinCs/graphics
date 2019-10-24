using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GLab1
{
    class Point
    {
        private int x;
        private int y;

        public int X { get => x; set => x = value; }
        public int Y { get => y; set => y = value; }

        public Point(int x, int y)
        {
            this.x = x;
            this.Y = y;
        }

        public override string ToString()
        {
            return "Point(X=" + this.x + ", Y=" + this.y + ')';
        }
    }
}
