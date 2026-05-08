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
    public class ReservationControl : BaseManagementControl
    {
        private TextBox txtId, txtDate, txtTime, txtGuests, txtStatus, txtCustomerId, txtTableId;

        public ReservationControl(MainDashboard dash) : base(dash)
        {
            GradientTop = Color.White;
            GradientBottom = Color.FromArgb(235, 255, 250);
            InitializeUI();
        }

        private void InitializeUI()
        {
            MakeTitle("Reservations");

            txtId = MakeField("Reservation ID", 40, 90, 10);
            txtDate = MakeField("Date (yyyy-MM-dd)", 360, 90, 10);
            txtTime = MakeField("Time (HH:mm)", 40, 165, 5);
            txtGuests = MakeField("Number of Guests", 360, 165, 5);
            txtStatus = MakeField("Status", 40, 240, 20);
            txtCustomerId = MakeField("Customer ID", 360, 240, 10);
            txtTableId = MakeField("Table ID", 40, 315, 10);

            MakeActionButton("Insert", 40, 385, BtnInsert_Click);
            MakeActionButton("Update", 180, 385, BtnUpdate_Click);

            DataGridView grid = BuildGrid(240,
                "ResID|ID",
                "Date|Date",
                "Time|Time",
                "Guests|Guests",
                "Status|Status",
                "CustID|Customer ID",
                "TableID|Table ID");

            grid.Rows.Add("1", "2026-06-01", "19:00", "4", "Confirmed", "1", "1");

            this.Controls.Add(grid);
        }

        private void BtnInsert_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: ID is required.", false); return; }
            if (!DateTime.TryParse(txtDate.Text, out DateTime date))
            { MainDash.ShowToast("Error: Date must be yyyy-MM-dd format.", false); return; }
            if (!TimeSpan.TryParse(txtTime.Text, out TimeSpan time))
            { MainDash.ShowToast("Error: Time must be HH:mm format.", false); return; }
            if (!int.TryParse(txtGuests.Text, out int guests) || guests <= 0)
            { MainDash.ShowToast("Error: Number of Guests must be a positive integer.", false); return; }
            if (string.IsNullOrWhiteSpace(txtStatus.Text)) { MainDash.ShowToast("Error: Status is required.", false); return; }
            if (!int.TryParse(txtCustomerId.Text, out int custId))
            { MainDash.ShowToast("Error: Customer ID must be an integer.", false); return; }
            if (!int.TryParse(txtTableId.Text, out int tableId))
            { MainDash.ShowToast("Error: Table ID must be an integer.", false); return; }

            Grid.Rows.Add(txtId.Text, date.ToString("yyyy-MM-dd"), time.ToString(@"hh\:mm"),
                          guests.ToString(), txtStatus.Text, custId.ToString(), tableId.ToString());
            ClearFields();
            MainDash.ShowToast("Reservation added successfully!", true);
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtId.Text)) { MainDash.ShowToast("Error: Enter the Reservation ID to update.", false); return; }
            DataGridViewRow row = FindRowById(txtId.Text);
            if (row == null) { MainDash.ShowToast($"Error: No reservation with ID '{txtId.Text}' found.", false); return; }

            if (DateTime.TryParse(txtDate.Text, out DateTime d)) row.Cells["Date"].Value = d.ToString("yyyy-MM-dd");
            if (TimeSpan.TryParse(txtTime.Text, out TimeSpan t)) row.Cells["Time"].Value = t.ToString(@"hh\:mm");
            if (int.TryParse(txtGuests.Text, out int g) && g > 0) row.Cells["Guests"].Value = g.ToString();
            if (!string.IsNullOrWhiteSpace(txtStatus.Text)) row.Cells["Status"].Value = txtStatus.Text;
            if (int.TryParse(txtCustomerId.Text, out int cid)) row.Cells["CustID"].Value = cid.ToString();
            if (int.TryParse(txtTableId.Text, out int tid)) row.Cells["TableID"].Value = tid.ToString();

            ClearFields();
            MainDash.ShowToast("Reservation updated successfully!", true);
        }

        private void ClearFields()
        {
            foreach (Control c in this.Controls)
                if (c is TextBox tb) tb.Clear();
        }
    }
}
