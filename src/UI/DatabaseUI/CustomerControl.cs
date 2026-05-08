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
    public class CustomerControl : BaseManagementControl
    {
        private TextBox txtId, txtEmail, txtFirstName, txtLastName, txtPhone, txtDateCreated;

        public CustomerControl(MainDashboard dash) : base(dash)
        {
            GradientTop = Color.White;
            GradientBottom = Color.FromArgb(230, 243, 255);
            InitializeUI();
        }

        private void InitializeUI()
        {
            MakeTitle("Customers");

            txtId = MakeField("Customer ID", 40, 90, 10);
            txtFirstName = MakeField("First Name", 360, 90, 50);
            txtLastName = MakeField("Last Name", 40, 165, 50);
            txtEmail = MakeField("Email", 360, 165, 100);
            txtPhone = MakeField("Phone", 40, 240, 15);
            txtDateCreated = MakeField("Date Created (yyyy-MM-dd)", 360, 240, 10);

            MakeActionButton("Insert", 40, 325, BtnInsert_Click);
            MakeActionButton("Update", 180, 325, BtnUpdate_Click);

            DataGridView grid = BuildGrid(270,
                "CustID|ID",
                "FirstName|First Name",
                "LastName|Last Name",
                "Email|Email",
                "Phone|Phone",
                "DateCreated|Date Created");

            // Seed rows
            grid.Rows.Add("1", "John", "Doe", "john@example.com", "555-0101", "2026-01-01");
            grid.Rows.Add("2", "Jane", "Smith", "jane@example.com", "555-0102", "2026-02-15");

            this.Controls.Add(grid);
        }

        private void BtnInsert_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: ID is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtFirstName.Text)) { MainDash.ShowToast("Error: First Name is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtLastName.Text)) { MainDash.ShowToast("Error: Last Name is required.", false); return; }
            if (string.IsNullOrWhiteSpace(txtPhone.Text)) { MainDash.ShowToast("Error: Phone is required.", false); return; }
            if (!DateTime.TryParse(txtDateCreated.Text, out DateTime date))
            { MainDash.ShowToast("Error: Date Created must be a valid date (yyyy-MM-dd).", false); return; }

            Grid.Rows.Add(txtId.Text, txtFirstName.Text, txtLastName.Text,
                          txtEmail.Text, txtPhone.Text, date.ToString("yyyy-MM-dd"));
            ClearFields();
            MainDash.ShowToast("Customer added successfully!", true);
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: Enter the ID of the customer to update.", false); return; }
            DataGridViewRow row = FindRowById(txtId.Text);
            if (row == null) { MainDash.ShowToast($"Error: No customer with ID '{txtId.Text}' found.", false); return; }

            if (!string.IsNullOrWhiteSpace(txtFirstName.Text)) row.Cells["FirstName"].Value = txtFirstName.Text;
            if (!string.IsNullOrWhiteSpace(txtLastName.Text)) row.Cells["LastName"].Value = txtLastName.Text;
            if (!string.IsNullOrWhiteSpace(txtEmail.Text)) row.Cells["Email"].Value = txtEmail.Text;
            if (!string.IsNullOrWhiteSpace(txtPhone.Text)) row.Cells["Phone"].Value = txtPhone.Text;
            if (DateTime.TryParse(txtDateCreated.Text, out DateTime d)) row.Cells["DateCreated"].Value = d.ToString("yyyy-MM-dd");

            ClearFields();
            MainDash.ShowToast("Customer updated successfully!", true);
        }

        private void ClearFields()
        {
            foreach (Control c in this.Controls)
                if (c is TextBox tb) tb.Clear();
        }
    }
}
