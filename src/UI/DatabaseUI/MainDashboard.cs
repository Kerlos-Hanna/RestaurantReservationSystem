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
    public class MainDashboard : Form
    {
        private Panel sidebarPanel;
        private Panel contentPanel;
        private Label lblToast;
        private Timer toastTimer;

        // Decorative circle data: (cx%, cy%, radius, argb color)
        private static readonly (float cx, float cy, int r, Color col)[] Bubbles =
        {
            (0.20f, 0.10f, 80,  Color.FromArgb(40,   0, 200, 230)),
            (0.75f, 0.25f, 55,  Color.FromArgb(35,  30, 160, 220)),
            (0.10f, 0.55f, 100, Color.FromArgb(30,   0, 230, 220)),
            (0.80f, 0.60f, 65,  Color.FromArgb(40,   0, 180, 200)),
            (0.50f, 0.82f, 90,  Color.FromArgb(25,  80, 200, 240)),
            (0.30f, 0.95f, 50,  Color.FromArgb(35,   0, 210, 200)),
            (0.90f, 0.92f, 70,  Color.FromArgb(30,  50, 170, 220)),
        };

        public MainDashboard()
        {
            InitializeUI();
        }

        private void InitializeUI()
        {
            this.Text = "Restaurant Manager";
            this.Size = new Size(1150, 720);
            this.MinimumSize = new Size(900, 600);
            this.StartPosition = FormStartPosition.CenterScreen;
            this.BackColor = Color.FromArgb(235, 242, 255);

            // ── Sidebar ──────────────────────────────────────────────────────
            sidebarPanel = new Panel();
            sidebarPanel.Dock = DockStyle.Left;
            sidebarPanel.Width = 240;
            sidebarPanel.Paint += SidebarPanel_Paint;

            // App title
            Label lblTitle = new Label();
            lblTitle.Text = "Restaurant Manager";
            lblTitle.Font = new Font("Segoe UI", 13, FontStyle.Bold);
            lblTitle.ForeColor = Color.White;
            lblTitle.AutoSize = false;
            lblTitle.Size = new Size(220, 46);
            lblTitle.TextAlign = ContentAlignment.MiddleCenter;
            lblTitle.Location = new Point(10, 22);
            lblTitle.BackColor = Color.Transparent;
            sidebarPanel.Controls.Add(lblTitle);

            // Thin horizontal divider under title
            Panel divider = new Panel();
            divider.BackColor = Color.FromArgb(70, 255, 255, 255);
            divider.Size = new Size(200, 1);
            divider.Location = new Point(20, 74);
            sidebarPanel.Controls.Add(divider);

            // ── Navigation buttons ──
            string[] labels = { "Customers", "Restaurants", "Staff", "Tables", "Reservations" };
            Action[] actions =
            {
                () => LoadControl(new CustomerControl(this)),
                () => LoadControl(new RestaurantControl(this)),
                () => LoadControl(new StaffControl(this)),
                () => LoadControl(new TableControl(this)),
                () => LoadControl(new ReservationControl(this)),
            };

            int btnY = 96;
            for (int i = 0; i < labels.Length; i++)
            {
                Action captured = actions[i];
                ModernButton btn = CreateNavButton(labels[i], btnY);
                btn.Click += (s, e) => captured();
                sidebarPanel.Controls.Add(btn);
                btnY += 64;
            }

            // ── Content Panel ──────────────────────────────────────────────
            contentPanel = new Panel();
            contentPanel.Dock = DockStyle.Fill;
            contentPanel.BackColor = Color.FromArgb(235, 242, 255);

            // Toast notification
            lblToast = new Label();
            lblToast.AutoSize = false;
            lblToast.Size = new Size(520, 42);
            lblToast.Font = new Font("Segoe UI", 11, FontStyle.Bold);
            lblToast.ForeColor = Color.White;
            lblToast.TextAlign = ContentAlignment.MiddleCenter;
            lblToast.Visible = false;
            contentPanel.Controls.Add(lblToast);

            toastTimer = new Timer();
            toastTimer.Interval = 3000;
            toastTimer.Tick += (s, e) => { lblToast.Visible = false; toastTimer.Stop(); };

            // Add in correct order (sidebar docks LEFT, content fills remaining)
            this.Controls.Add(contentPanel);
            this.Controls.Add(sidebarPanel);

            LoadControl(new CustomerControl(this));
        }

        // ── Sidebar paint: dark-blue gradient + floating translucent circles ──
        private void SidebarPanel_Paint(object sender, PaintEventArgs e)
        {
            int w = sidebarPanel.Width;
            int h = sidebarPanel.Height;
            if (w < 2 || h < 2) return;

            Graphics g = e.Graphics;
            g.SmoothingMode = SmoothingMode.AntiAlias;

            // Base gradient: dark navy → rich teal-blue
            using (LinearGradientBrush bg = new LinearGradientBrush(
                sidebarPanel.ClientRectangle,
                Color.FromArgb(10, 25, 80),
                Color.FromArgb(0, 100, 170),
                LinearGradientMode.Vertical))
            {
                g.FillRectangle(bg, sidebarPanel.ClientRectangle);
            }

            // Floating decorative circles
            foreach (var (cx, cy, r, col) in Bubbles)
            {
                int x = (int)(cx * w) - r;
                int y = (int)(cy * h) - r;
                int d = r * 2;

                // Outer glow ring
                using (GraphicsPath gp = new GraphicsPath())
                {
                    gp.AddEllipse(x, y, d, d);
                    using (PathGradientBrush pgb = new PathGradientBrush(gp))
                    {
                        pgb.CenterColor = Color.FromArgb(col.A + 20 > 255 ? 255 : col.A + 20, col);
                        pgb.SurroundColors = new[] { Color.FromArgb(0, col) };
                        g.FillPath(pgb, gp);
                    }
                }

                // Filled inner circle (slightly smaller, more opaque)
                int inset = r / 4;
                using (SolidBrush sb = new SolidBrush(Color.FromArgb(col.A, col)))
                    g.FillEllipse(sb, x + inset, y + inset, d - inset * 2, d - inset * 2);
            }
        }

        // ── Nav button factory ──────────────────────────────────────────────
        private ModernButton CreateNavButton(string text, int yPos)
        {
            ModernButton btn = new ModernButton();
            btn.Text = text;
            btn.Size = new Size(210, 50);
            btn.Location = new Point(15, yPos);
            btn.BorderRadius = 16;
            btn.Font = new Font("Segoe UI", 10, FontStyle.Bold);

            // Solid, readable cyan-to-teal gradient (dark enough for white text)
            btn.Color1 = Color.FromArgb(0, 172, 193);   // cyan 700
            btn.Color2 = Color.FromArgb(0, 120, 160);   // teal-blue
            btn.HoverColor1 = Color.FromArgb(30, 210, 230);  // lighter cyan
            btn.HoverColor2 = Color.FromArgb(0, 160, 200);

            btn.NormalTextColor = Color.White;
            btn.HoverTextColor = Color.White;

            // BackColor = Transparent so Region clipping lets the sidebar gradient show through corners
            btn.BackColor = Color.Transparent;
            return btn;
        }

        // ── Load a UserControl into the content area ──────────────────────
        public void LoadControl(UserControl control)
        {
            for (int i = contentPanel.Controls.Count - 1; i >= 0; i--)
                if (contentPanel.Controls[i] != lblToast)
                    contentPanel.Controls.RemoveAt(i);

            control.Dock = DockStyle.Fill;
            contentPanel.Controls.Add(control);
            lblToast.BringToFront();
        }

        // ── Toast banner ──────────────────────────────────────────────────
        public void ShowToast(string message, bool isSuccess)
        {
            lblToast.Text = message;
            lblToast.BackColor = isSuccess
                ? Color.FromArgb(34, 139, 87)
                : Color.FromArgb(200, 60, 60);
            lblToast.Location = new Point((contentPanel.Width - lblToast.Width) / 2, 16);
            lblToast.Visible = true;
            lblToast.BringToFront();
            toastTimer.Stop();
            toastTimer.Start();
        }
    }
}
