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
    /// Base class for all management UserControls.
    /// Handles: safe gradient background, action button factory, shared grid setup.
    /// </summary>
    public abstract class BaseManagementControl : UserControl
    {
        protected MainDashboard MainDash { get; }
        protected DataGridView Grid { get; private set; }

        // Subclasses set these gradient colours in their constructor before InitializeUI()
        protected Color GradientTop = Color.White;
        protected Color GradientBottom = Color.FromArgb(225, 240, 255);

        protected BaseManagementControl(MainDashboard dash)
        {
            MainDash = dash;
            this.SetStyle(ControlStyles.ResizeRedraw | ControlStyles.OptimizedDoubleBuffer, true);
            this.Font = new Font("Segoe UI", 10);
        }

        // ── Safe gradient background (guards against 0-size crash) ──
        protected override void OnPaintBackground(PaintEventArgs e)
        {
            if (this.ClientRectangle.Width < 2 || this.ClientRectangle.Height < 2)
            {
                base.OnPaintBackground(e);
                return;
            }
            using (LinearGradientBrush brush = new LinearGradientBrush(
                this.ClientRectangle, GradientTop, GradientBottom, LinearGradientMode.Vertical))
            {
                e.Graphics.FillRectangle(brush, this.ClientRectangle);
            }
        }

        // ── Shared: add a styled DataGridView with a Delete button column ──
        protected DataGridView BuildGrid(int bottomHeight, params string[] colDefs)
        {
            Grid = new DataGridView();
            Grid.Dock = DockStyle.Bottom;
            Grid.Height = bottomHeight;
            Grid.BackgroundColor = Color.White;
            Grid.BorderStyle = BorderStyle.None;
            Grid.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            Grid.AllowUserToAddRows = false;
            Grid.RowHeadersVisible = false;
            Grid.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            Grid.Font = new Font("Segoe UI", 9);
            Grid.ColumnHeadersDefaultCellStyle.Font = new Font("Segoe UI", 9, FontStyle.Bold);
            Grid.ColumnHeadersDefaultCellStyle.BackColor = Color.FromArgb(0, 120, 212);
            Grid.ColumnHeadersDefaultCellStyle.ForeColor = Color.White;
            Grid.EnableHeadersVisualStyles = false;
            Grid.AlternatingRowsDefaultCellStyle.BackColor = Color.FromArgb(240, 247, 255);
            Grid.CellClick += Grid_CellClick;

            // Add data columns from "Key|Header" pairs
            foreach (string def in colDefs)
            {
                string[] parts = def.Split('|');
                Grid.Columns.Add(parts[0], parts[1]);
            }

            // Delete action column
            DataGridViewButtonColumn deleteCol = new DataGridViewButtonColumn();
            deleteCol.Name = "DeleteAction";
            deleteCol.HeaderText = "Action";
            deleteCol.Text = "Delete";
            deleteCol.UseColumnTextForButtonValue = true;
            deleteCol.AutoSizeMode = DataGridViewAutoSizeColumnMode.None;
            deleteCol.Width = 80;
            deleteCol.DefaultCellStyle.BackColor = Color.FromArgb(200, 60, 60);
            deleteCol.DefaultCellStyle.ForeColor = Color.White;
            deleteCol.DefaultCellStyle.Font = new Font("Segoe UI", 9, FontStyle.Bold);
            Grid.Columns.Add(deleteCol);

            return Grid;
        }

        private void Grid_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0 && e.ColumnIndex == Grid.Columns["DeleteAction"].Index)
            {
                Grid.Rows.RemoveAt(e.RowIndex);
                MainDash.ShowToast("Record deleted successfully.", true);
            }
        }

        // ── Action button factory ──
        protected ModernButton MakeActionButton(string text, int x, int y, EventHandler onClick)
        {
            ModernButton btn = new ModernButton();
            btn.Text = text;
            btn.Location = new Point(x, y);
            btn.Size = new Size(120, 42);
            btn.BorderRadius = 12;
            btn.Font = new Font("Segoe UI", 10, FontStyle.Bold);

            switch (text)
            {
                case "Insert":
                    btn.Color1 = Color.FromArgb(40, 167, 100);
                    btn.Color2 = Color.FromArgb(20, 130, 70);
                    btn.HoverColor1 = Color.FromArgb(60, 200, 120);
                    btn.HoverColor2 = Color.FromArgb(30, 160, 90);
                    btn.NormalTextColor = Color.FromArgb(230, 255, 230);
                    btn.HoverTextColor = Color.White;
                    btn.BackColor = GradientBottom;
                    break;
                case "Update":
                    btn.Color1 = Color.FromArgb(13, 110, 253);
                    btn.Color2 = Color.FromArgb(0, 70, 200);
                    btn.HoverColor1 = Color.FromArgb(50, 140, 255);
                    btn.HoverColor2 = Color.FromArgb(10, 90, 220);
                    btn.NormalTextColor = Color.FromArgb(220, 235, 255);
                    btn.HoverTextColor = Color.White;
                    btn.BackColor = GradientBottom;
                    break;
            }

            btn.Click += onClick;
            this.Controls.Add(btn);
            return btn;
        }

        // ── Label + TextBox factory ──
        protected TextBox MakeField(string labelText, int x, int y, int maxLen = 100)
        {
            Label lbl = new Label();
            lbl.Text = labelText;
            lbl.Location = new Point(x, y);
            lbl.AutoSize = true;
            lbl.ForeColor = Color.FromArgb(60, 70, 100);
            lbl.BackColor = Color.Transparent;
            lbl.Font = new Font("Segoe UI", 9, FontStyle.Regular);
            this.Controls.Add(lbl);

            TextBox txt = new TextBox();
            txt.Location = new Point(x, y + 22);
            txt.Size = new Size(280, 28);
            txt.BorderStyle = BorderStyle.FixedSingle;
            txt.MaxLength = maxLen;
            txt.BackColor = Color.White;
            txt.Font = new Font("Segoe UI", 10);
            this.Controls.Add(txt);
            return txt;
        }

        // ── Section title ──
        protected void MakeTitle(string text)
        {
            Label lbl = new Label();
            lbl.Text = text;
            lbl.Font = new Font("Segoe UI Light", 22);
            lbl.ForeColor = Color.FromArgb(20, 40, 100);
            lbl.AutoSize = true;
            lbl.Location = new Point(40, 28);
            lbl.BackColor = Color.Transparent;
            this.Controls.Add(lbl);
        }

        // ── Helper: find row by ID column (first col) ──
        protected DataGridViewRow FindRowById(string id)
        {
            foreach (DataGridViewRow row in Grid.Rows)
            {
                if (row.Cells[0].Value?.ToString() == id)
                    return row;
            }
            return null;
        }
    }
}
