package clinic.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;


/**
 * 病人基本信息
 */
@Entity
@NamedQuery(name="Patient.all", query="select p from Patient as p order by p.name")
public class Patient extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1290170428250095532L;
    // 姓名
    private String name;
    // 年龄
    private int age;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "GENDER")
    private Sex gender;
    // 联系地址
    private String address;
    // 电话
    private String phone;
    // 医保卡号
    private String cardID;

    // 就诊的状态      0:挂号 1：开好处方  2:抓好药
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    private int status;

    public Patient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Sex getGender() {
        return gender;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final Patient other = (Patient) obj;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Patient[name=" + name + ",age=" + age + "]";
    }
}
