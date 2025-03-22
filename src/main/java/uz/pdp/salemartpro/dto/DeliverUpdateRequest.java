package uz.pdp.salemartpro.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliverUpdateRequest {
    private String username;
    private String email;
    private String phone;
    private String vehicleNumber;

    // Используем аннотацию для явного указания имени свойства в JSON
    @JsonProperty("online")
    private Boolean online;

    // Геттеры и сеттеры
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "DeliverUpdateRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", online=" + online +
                '}';
    }
}