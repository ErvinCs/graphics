using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GLab1
{
    class ViewPort
    {
        private int u1, v1, u2, v2;

        public int U1 { get => u1; set => u1 = value; }
        public int V1 { get => v1; set => v1 = value; }
        public int U2 { get => u2; set => u2 = value; }
        public int V2 { get => v2; set => v2 = value; }

        public ViewPort(int x1, int y1, int x2, int y2)
        {
            this.u1 = x1;
            this.v1 = y1;
            this.u2 = x2;
            this.v2 = y2;
        }

        public override string ToString()
        {
            return "ViewPort(u1=" + u1 + ", v1=" + v1 +
                    "u2=" + u2 + ", v2=" + v2 + ')';
        }
    }
}
