package com.example.testapp1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskDTO {

    public static class InputDTO {
        @SerializedName("mode")
        @Expose
        private String mode;

        @SerializedName("userName")
        @Expose
        private String userName;

        @SerializedName("gender")
        @Expose
        private String gender;

        @SerializedName("age")
        @Expose
        private String age;

        @SerializedName("phoneNumber")
        @Expose
        private String phoneNumber;

        @SerializedName("fileName0")
        @Expose
        private String fileName0;

        @SerializedName("fileName1")
        @Expose
        private String fileName1;

        @SerializedName("fileName2")
        @Expose
        private String fileName2;

        @SerializedName("fileName3")
        @Expose
        private String fileName3;

        @SerializedName("fileName4")
        @Expose
        private String fileName4;

        @SerializedName("fileName5")
        @Expose
        private String fileName5;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFileName0() {
            return fileName0;
        }

        public void setFileName0(String fileName0) {
            this.fileName0 = fileName0;
        }

        public String getFileName1() {
            return fileName1;
        }

        public void setFileName1(String fileName1) {
            this.fileName1 = fileName1;
        }

        public String getFileName2() {
            return fileName2;
        }

        public void setFileName2(String fileName2) {
            this.fileName2 = fileName2;
        }

        public String getFileName3() {
            return fileName3;
        }

        public void setFileName3(String fileName3) {
            this.fileName3 = fileName3;
        }

        public String getFileName4() {
            return fileName4;
        }

        public void setFileName4(String fileName4) {
            this.fileName4 = fileName4;
        }

        public String getFileName5() {
            return fileName5;
        }

        public void setFileName5(String fileName5) {
            this.fileName5 = fileName5;
        }
    }

    public static class OutputDTO {
        private modeType mode;
        private String result;

        public modeType getMode() {
            return mode;
        }

        public void setMode(modeType mode) {
            this.mode = mode;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public enum modeType {
            INSERT,
            UPDATE,
            SELECT,
            DELETE
        }
    }
}
