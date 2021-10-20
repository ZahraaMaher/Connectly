package com.example.connectly.jobprovider;

public class Job {

    String providerEmail;
    String jobProviderName;
    String jobTitle;
    String jobDescription;
    String jobRequirements;
    String jobLocation;
    String seekerEmail;

    public Job() {
        providerEmail = "";
        jobProviderName = "";
        jobTitle = "";
        jobDescription = "";
        jobRequirements = "";
        jobLocation = "";
        seekerEmail = "";
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public String getJobProviderName() {
        return jobProviderName;
    }

    public void setJobProviderName(String jobProviderName) {
        this.jobProviderName = jobProviderName;
    }


    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }

    public void setJobRequirements(String jobRequirements) {
        this.jobRequirements = jobRequirements;
    }

    public void setJobLocation(String _address) {
        this.jobLocation = _address;
    }

    public String getJobLocation() {
        return this.jobLocation;
    }

    public String getSeekerEmail() {
        return seekerEmail;
    }

    public void setSeekerEmail(String seekerEmail) {
        this.seekerEmail = seekerEmail;
    }




}
