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
    public class StaffControl : BaseManagementControl
    {
        private TextBox txtId, txtFirstName, txtLastName, txtRole, txtSalary, txtPhone, txtRestaurantId;

        public StaffControl(MainDashboard dash) : base(dash)
        {
            GradientTop = Color.White;
            GradientBottom = Color.FromArgb(255, 250, 235);
            InitializeUI();
        }

        private void InitializeUI()
        {
            MakeTitle("Staff");

            txtId = MakeField("Staff ID", 40, 90, 10);
            txtFirstName = MakeField("First Name", 360, 90, 50);
            txtLastName = MakeField("Last Name", 40, 165, 50);
            txtRole = MakeField("Role", 360, 165, 50);
            txtSalary = MakeField("Salary (0.00)", 40, 240, 15);
            txtPhone = MakeField("Phone", 360, 240, 15);
            txtRestaurantId = MakeField("Restaurant ID", 40, 315, 10);

            MakeActionButton("Insert", 40, 385, BtnInsert_Click);
            MakeActionButton("Update", 180, 385, BtnUpdate_Click);

            DataGridView grid = BuildGrid(240,
                "StaffID|ID",
                "FirstName|First Name",
                "LastName|Last Name",
                "Role|Role",
                "Salary|Salary",
                "Phone|Phone",
                "RestID|Restaurant ID");

            grid.Rows.Add("1", "Alice", "Brown", "Manager", "5000.00", "555-9999", "1");

            this.Controls.Add(grid);
        }

        private void BtnInsert_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: ID is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtFirstName.Text)) { MainDash.ShowToast("Error: First Name is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtLastName.Text)) { MainDash.ShowToast("Error: Last Name is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtRole.Text)) { MainDash.ShowToast("Error: Role is required.", false); return; }
            if (!decimal.TryParse(txtSalary.Text, out decimal salary))
            { MainDash.ShowToast("Error: Salary must be a valid decimal number.", false); return; }
            if (!int.TryParse(txtRestaurantId.Text, out int restId))
            { MainDash.ShowToast("Error: Restaurant ID must be an integer.", false); return; }

            Grid.Rows.Add(txtId.Text, txtFirstName.Text, txtLastName.Text,
                          txtRole.Text, salary.ToString("0.00"), txtPhone.Text, restId.ToString());
            ClearFields();
            MainDash.ShowToast("Staff member added successfully!", true);
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: Enter the Staff ID to update.", false); return; }
            DataGridViewRow row = FindRowById(txtId.Text);
            if (row == null) { MainDash.ShowToast($"Error: No staff with ID '{txtId.Text}' found.", false); return; }

            if (!string.IsNullOrWhiteSpace(txtFirstName.Text)) row.Cells["FirstName"].Value = txtFirstName.Text;
            if (!string.IsNullOrWhiteSpace(txtLastName.Text)) row.Cells["LastName"].Value = txtLastName.Text;
            if (!string.IsNullOrWhiteSpace(txtRole.Text)) row.Cells["Role"].Value = txtRole.Text;
            if (decimal.TryParse(txtSalary.Text, out decimal s)) row.Cells["Salary"].Value = s.ToString("0.00");
            if (!string.IsNullOrWhiteSpace(txtPhone.Text)) row.Cells["Phone"].Value = txtPhone.Text;
            if (int.TryParse(txtRestaurantId.Text, out int rid)) row.Cells["RestID"].Value = rid.ToString();

            ClearFields();
            MainDash.ShowToast("Staff member updated successfully!", true);
        }

        private void ClearFields()
        {
            foreach (Control c in this.Controls)
                if (c is TextBox tb) tb.Clear();
        }
    }

}
