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
    public class TableControl : BaseManagementControl
    {
        private TextBox txtId, txtTableNumber, txtCapacity, txtLocation, txtRestaurantId;

        public TableControl(MainDashboard dash) : base(dash)
        {
            GradientTop = Color.White;
            GradientBottom = Color.FromArgb(235, 235, 255);
            InitializeUI();
        }

        private void InitializeUI()
        {
            MakeTitle("Tables");

            txtId = MakeField("Table ID", 40, 90, 10);
            txtTableNumber = MakeField("Table Number", 360, 90, 10);
            txtCapacity = MakeField("Capacity", 40, 165, 5);
            txtLocation = MakeField("Location", 360, 165, 50);
            txtRestaurantId = MakeField("Restaurant ID", 40, 240, 10);

            MakeActionButton("Insert", 40, 315, BtnInsert_Click);
            MakeActionButton("Update", 180, 315, BtnUpdate_Click);

            DataGridView grid = BuildGrid(270,
                "TableID|ID",
                "TableNum|Table Number",
                "Capacity|Capacity",
                "Location|Location",
                "RestID|Restaurant ID");

            grid.Rows.Add("1", "T-01", "4", "Window", "1");
            grid.Rows.Add("2", "T-02", "2", "Patio", "1");

            this.Controls.Add(grid);
        }

        private void BtnInsert_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: ID is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtTableNumber.Text)) { MainDash.ShowToast("Error: Table Number is required.", false); return; }
            if (!int.TryParse(txtCapacity.Text, out int capacity) || capacity <= 0)
            { MainDash.ShowToast("Error: Capacity must be a positive integer.", false); return; }
            if (!int.TryParse(txtRestaurantId.Text, out int restId))
            { MainDash.ShowToast("Error: Restaurant ID must be an integer.", false); return; }

            Grid.Rows.Add(txtId.Text, txtTableNumber.Text, capacity.ToString(), txtLocation.Text, restId.ToString());
            ClearFields();
            MainDash.ShowToast("Table added successfully!", true);
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: Enter the Table ID to update.", false); return; }
            DataGridViewRow row = FindRowById(txtId.Text);
            if (row == null) { MainDash.ShowToast($"Error: No table with ID '{txtId.Text}' found.", false); return; }

            if (!string.IsNullOrWhiteSpace(txtTableNumber.Text)) row.Cells["TableNum"].Value = txtTableNumber.Text;
            if (int.TryParse(txtCapacity.Text, out int cap) && cap > 0) row.Cells["Capacity"].Value = cap.ToString();
            if (!string.IsNullOrWhiteSpace(txtLocation.Text)) row.Cells["Location"].Value = txtLocation.Text;
            if (int.TryParse(txtRestaurantId.Text, out int rid)) row.Cells["RestID"].Value = rid.ToString();

            ClearFields();
            MainDash.ShowToast("Table updated successfully!", true);
        }

        private void ClearFields()
        {
            foreach (Control c in this.Controls)
                if (c is TextBox tb) tb.Clear();
        }
    }
}
