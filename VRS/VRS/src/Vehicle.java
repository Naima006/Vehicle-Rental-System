import java.io.Serializable;

enum VehicleType
{
    SEDAN, SUV, MINIBUS, AMBULANCE, MINIVAN, TRUCK
}

class Vehicle implements Serializable
{
    private String vehicleID;
    private VehicleType type;
    private String make;
    private String model;
    private RentalStatus status;
    private String cost;
    private String imagePath;

    public Vehicle(String vehicleID, VehicleType type, String make, String model, RentalStatus status, String cost, String imagePath)
    {
        this.vehicleID = vehicleID;
        this.type = type;
        this.make = make;
        this.model = model;
        this.status = status;
        this.cost = cost;
        this.imagePath = imagePath;
    }

    public void setVehicleID(String vehicleID)
    {
        this.vehicleID = vehicleID;
    }

    public String getVehicleID()
    {
        return vehicleID;
    }

    public void setType(VehicleType type)
    {
        this.type = type;
    }

    public VehicleType getType()
    {
        return type;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public String getMake()
    {
        return make;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return model;
    }

    public void setStatus(RentalStatus status)
    {
        this.status = status;
    }

    public RentalStatus getStatus()
    {
        return status;
    }

    public void setCost(String cost)
    {
        this.cost = cost;
    }

    public String getCost()
    {
        return cost;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getImagePath()
    {
        return imagePath;
    }
}
