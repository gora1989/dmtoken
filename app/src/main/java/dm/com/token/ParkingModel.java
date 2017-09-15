package dm.com.token;

/**
 * Created by gora3 on 5/17/17.
 */

public class ParkingModel {

    String id;
    String name;

    String phone;
    String vehicle_no;
    String token;
    Long parkingTimestamp;
    Long deliveryTimestamp;
    String status;

    public ParkingModel(String id, String name, String phone, String vehicle_no, String token,
                        String status, Long parkingTimestamp, Long deliveryTimestamp) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.vehicle_no = vehicle_no;
        this.token = token;
        this.status = status;
        this.parkingTimestamp = parkingTimestamp;
        this.deliveryTimestamp = deliveryTimestamp;
    }

    public ParkingModel() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public String getToken() {
        return token;
    }

    public Long getParkingTimestamp() {
        return parkingTimestamp;
    }

    public Long getDeliveryTimestamp() {
        return deliveryTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setParkingTimestamp(Long parkingTimestamp) {
        this.parkingTimestamp = parkingTimestamp;
    }

    public void setDeliveryTimestamp(Long deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
