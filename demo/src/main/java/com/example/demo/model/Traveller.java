package com.example.demo.model;

public class Traveller {
    private Integer traveller_id;
    private String first_name;
    private String last_name;
    private String phone;
    private String email;
    private String emergency_contact_first_name;
    private String emergency_contact_last_name;
    private String emergency_contact_no;
    private String id_proof_type;
    private String id_proof_number;

    public Traveller() {}

    public Traveller(Integer traveller_id, String first_name, String last_name, String phone, String email,
                     String emergency_contact_first_name, String emergency_contact_last_name,
                     String emergency_contact_no, String id_proof_type, String id_proof_number) {
        this.traveller_id = traveller_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.emergency_contact_first_name = emergency_contact_first_name;
        this.emergency_contact_last_name = emergency_contact_last_name;
        this.emergency_contact_no = emergency_contact_no;
        this.id_proof_type = id_proof_type;
        this.id_proof_number = id_proof_number;
    }

    public Integer getTraveller_id() { return traveller_id; }
    public void setTraveller_id(Integer traveller_id) { this.traveller_id = traveller_id; }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEmergency_contact_first_name() { return emergency_contact_first_name; }
    public void setEmergency_contact_first_name(String emergency_contact_first_name) { this.emergency_contact_first_name = emergency_contact_first_name; }

    public String getEmergency_contact_last_name() { return emergency_contact_last_name; }
    public void setEmergency_contact_last_name(String emergency_contact_last_name) { this.emergency_contact_last_name = emergency_contact_last_name; }

    public String getEmergency_contact_no() { return emergency_contact_no; }
    public void setEmergency_contact_no(String emergency_contact_no) { this.emergency_contact_no = emergency_contact_no; }

    public String getId_proof_type() { return id_proof_type; }
    public void setId_proof_type(String id_proof_type) { this.id_proof_type = id_proof_type; }

    public String getId_proof_number() { return id_proof_number; }
    public void setId_proof_number(String id_proof_number) { this.id_proof_number = id_proof_number; }

    @Override
    public String toString() {
        return "Traveller{" +
                "traveller_id=" + traveller_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", emergency_contact_first_name='" + emergency_contact_first_name + '\'' +
                ", emergency_contact_last_name='" + emergency_contact_last_name + '\'' +
                ", emergency_contact_no='" + emergency_contact_no + '\'' +
                ", id_proof_type='" + id_proof_type + '\'' +
                ", id_proof_number='" + id_proof_number + '\'' +
                '}';
    }
}
