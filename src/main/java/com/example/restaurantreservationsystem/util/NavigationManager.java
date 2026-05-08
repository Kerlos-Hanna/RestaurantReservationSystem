using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;

public class NavigationManager
{
    private static NavigationManager _instance;
    private static readonly object _lock = new object();
    private Form _primaryForm;

    protected NavigationManager() { }

    public static NavigationManager GetInstance()
    {
        if (_instance == null)
        {
            lock (_lock)
            {
                if (_instance == null)
                    _instance = new NavigationManager();
            }
        }
        return _instance;
    }

    public void SetPrimaryForm(Form form)
    {
        _primaryForm = form;
    }

    public void NavigateToDashboard()
    {
        LoadForm(new DashboardForm());
    }

    public void NavigateToCustomers()
    {
        LoadForm(new CustomersForm());
    }

    public void NavigateToReservations()
    {
        LoadForm(new ReservationsForm());
    }

    public void NavigateToTables()
    {
        LoadForm(new TablesForm());
    }

    public void NavigateToStaff()
    {
        LoadForm(new StaffForm());
    }

    private void LoadForm(Form form)
    {
        if (_primaryForm == null)
            throw new InvalidOperationException("Primary form has not been set.");

        List<Form> formsToClose = new List<Form>();
        foreach (Form f in Application.OpenForms)
            if (f != _primaryForm)
                formsToClose.Add(f);

        foreach (Form f in formsToClose)
            f.Close();

        form.Show();
        form.BringToFront();
    }
}

public class DashboardForm : Form
{
    public DashboardForm()
    {
        this.Text = "Dashboard";
        this.Size = new Size(800, 600);
    }
}

public class CustomersForm : Form
{
    public CustomersForm()
    {
        this.Text = "Customers";
        this.Size = new Size(800, 600);
    }
}

public class ReservationsForm : Form
{
    public ReservationsForm()
    {
        this.Text = "Reservations";
        this.Size = new Size(800, 600);
    }
}

public class TablesForm : Form
{
    public TablesForm()
    {
        this.Text = "Tables";
        this.Size = new Size(800, 600);
    }
}

public class StaffForm : Form
{
    public StaffForm()
    {
        this.Text = "Staff";
        this.Size = new Size(800, 600);
    }
}

public class NavigationManager_TestHelper : NavigationManager
{
    public NavigationManager_TestHelper() { }
}
