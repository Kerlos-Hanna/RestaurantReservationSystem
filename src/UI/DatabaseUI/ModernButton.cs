using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace DatabaseUI
{
    /// <summary>
    /// A fully custom-painted gradient button with proper rounded corners.
    /// Uses Region clipping so the parent background (even a gradient) shows
    /// through the corner areas naturally — no black or squared-edge artifacts.
    /// </summary>
    public class ModernButton : Button
    {
        private bool isHovered = false;

        // ── Configurable gradient + text colours ──
        public Color Color1 { get; set; } = Color.FromArgb(0, 172, 193);   // cyan
        public Color Color2 { get; set; } = Color.FromArgb(0, 105, 192);   // blue
        public Color HoverColor1 { get; set; } = Color.FromArgb(0, 210, 230);
        public Color HoverColor2 { get; set; } = Color.FromArgb(0, 140, 210);
        public Color NormalTextColor { get; set; } = Color.White;
        public Color HoverTextColor { get; set; } = Color.White;
        public int BorderRadius { get; set; } = 14;

        public ModernButton()
        {
            // Full owner-draw, no system chrome
            this.SetStyle(
                ControlStyles.UserPaint |
                ControlStyles.AllPaintingInWmPaint |
                ControlStyles.OptimizedDoubleBuffer |
                ControlStyles.SupportsTransparentBackColor,
                true);

            this.FlatStyle = FlatStyle.Flat;
            this.FlatAppearance.BorderSize = 0;
            this.BackColor = Color.Transparent;  // let parent gradient show through corners
            this.Font = new Font("Segoe UI", 10, FontStyle.Bold);
            this.Cursor = Cursors.Hand;
        }

        protected override void OnMouseEnter(EventArgs e)
        {
            base.OnMouseEnter(e);
            isHovered = true;
            this.Invalidate();
        }

        protected override void OnMouseLeave(EventArgs e)
        {
            base.OnMouseLeave(e);
            isHovered = false;
            this.Invalidate();
        }

        protected override void OnPaint(PaintEventArgs pe)
        {
            Graphics g = pe.Graphics;
            g.SmoothingMode = SmoothingMode.AntiAlias;
            g.PixelOffsetMode = PixelOffsetMode.HighQuality;

            Rectangle rect = this.ClientRectangle;
            if (rect.Width < 4 || rect.Height < 4) return;

            // 1px inset so the anti-aliased edge isn't clipped by the control boundary
            Rectangle drawRect = Rectangle.Inflate(rect, -1, -1);

            using (GraphicsPath path = RoundedRect(drawRect, BorderRadius))
            {
                // ── Clip the control to the rounded shape ──
                // This makes corners transparent, revealing whatever is behind
                // the button (even a gradient-painted panel).
                this.Region = new Region(path);

                Color c1 = isHovered ? HoverColor1 : Color1;
                Color c2 = isHovered ? HoverColor2 : Color2;

                using (LinearGradientBrush brush = new LinearGradientBrush(drawRect, c1, c2, 45f))
                {
                    g.FillPath(brush, path);
                }

                // Subtle inner glow on hover
                if (isHovered)
                {
                    using (Pen glowPen = new Pen(Color.FromArgb(80, Color.White), 1.5f))
                        g.DrawPath(glowPen, path);
                }
            }

            Color tc = isHovered ? HoverTextColor : NormalTextColor;
            TextRenderer.DrawText(g, this.Text, this.Font, rect, tc,
                TextFormatFlags.HorizontalCenter | TextFormatFlags.VerticalCenter | TextFormatFlags.WordBreak);
        }

        private static GraphicsPath RoundedRect(Rectangle r, int radius)
        {
            int d = Math.Min(radius * 2, Math.Min(r.Width, r.Height));
            GraphicsPath p = new GraphicsPath();
            p.StartFigure();
            p.AddArc(r.X, r.Y, d, d, 180, 90);
            p.AddArc(r.Right - d, r.Y, d, d, 270, 90);
            p.AddArc(r.Right - d, r.Bottom - d, d, d, 0, 90);
            p.AddArc(r.X, r.Bottom - d, d, d, 90, 90);
            p.CloseFigure();
            return p;
        }
    }
}
