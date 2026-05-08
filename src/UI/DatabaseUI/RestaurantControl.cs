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
    public class RestaurantControl : BaseManagementControl
    {
        private TextBox txtId, txtName, txtAddress, txtPhone, txtOpeningHours, txtClosingHours;

        public RestaurantControl(MainDashboard dash) : base(dash)
        {
            GradientTop = Color.White;
            GradientBottom = Color.FromArgb(230, 255, 245);
            InitializeUI();
        }

        private void InitializeUI()
        {
            MakeTitle("Restaurants");

            txtId = MakeField("Restaurant ID", 40, 90, 10);
            txtName = MakeField("Name", 360, 90, 100);
            txtAddress = MakeField("Address", 40, 165, 200);
            txtPhone = MakeField("Phone", 360, 165, 15);
            txtOpeningHours = MakeField("Opening Hours (HH:mm)", 40, 240, 5);
            txtClosingHours = MakeField("Closing Hours (HH:mm)", 360, 240, 5);

            MakeActionButton("Insert", 40, 325, BtnInsert_Click);
            MakeActionButton("Update", 180, 325, BtnUpdate_Click);

            DataGridView grid = BuildGrid(270,
                "RestID|ID",
                "Name|Name",
                "Address|Address",
                "Phone|Phone",
                "Opening|Opening",
                "Closing|Closing");

            grid.Rows.Add("1", "Main Branch", "123 Main St", "555-1234", "08:00", "22:00");

            this.Controls.Add(grid);
        }

        private void BtnInsert_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: ID is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtName.Text)) { MainDash.ShowToast("Error: Name is required.", false); return; }
            if (!TimeSpan.TryParse(txtOpeningHours.Text, out _))
            { MainDash.ShowToast("Error: Opening Hours must be HH:mm format.", false); return; }
            if (!TimeSpan.TryParse(txtClosingHours.Text, out _))
            { MainDash.ShowToast("Error: Closing Hours must be HH:mm format.", false); return; }

            Grid.Rows.Add(txtId.Text, txtName.Text, txtAddress.Text,
                          txtPhone.Text, txtOpeningHours.Text, txtClosingHours.Text);
            ClearFields();
            MainDash.ShowToast("Restaurant added successfully!", true);
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: Enter the ID of the restaurant to update.", false); return; }
            DataGridViewRow row = FindRowById(txtId.Text);
            if (row == null) { MainDash.ShowToast($"Error: No restaurant with ID '{txtId.Text}' found.", false); return; }

            if (!string.IsNullOrWhiteSpace(txtName.Text)) row.Cells["Name"].Value = txtName.Text;
            if (!string.IsNullOrWhiteSpace(txtAddress.Text)) row.Cells["Address"].Value = txtAddress.Text;
            if (!string.IsNullOrWhiteSpace(txtPhone.Text)) row.Cells["Phone"].Value = txtPhone.Text;
            if (TimeSpan.TryParse(txtOpeningHours.Text, out _)) row.Cells["Opening"].Value = txtOpeningHours.Text;
            if (TimeSpan.TryParse(txtClosingHours.Text, out _)) row.Cells["Closing"].Value = txtClosingHours.Text;

            ClearFields();
            MainDash.ShowToast("Restaurant updated successfully!", true);
        }

        private void ClearFields()
        {
            foreach (Control c in this.Controls)
                if (c is TextBox tb) tb.Clear();
        }
    }

}
