import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';


@Injectable({
    providedIn: "root"
})
export class MyCompetenceService {
    //API_URL = "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/";
    // API_URL = "http://100.96.32.55:8080/isf-rest-server-java/";
    // API_URL="https://isfsitservices.internal.ericsson.com:8443/isf-rest-server-java_sit_major/";
    API_URL = environment.GLOBAL_API_URL; 
    constructor(private http: HttpClient) { }


    getBaseline() {
        return this.http.get(this.API_URL + "competenceController/getBaseline");

    }

    getTechnology(compTypeId, domainId) {
        return this.http.get(this.API_URL + "competenceController/getTechnology/" + compTypeId + "/" + domainId);
    }

    getVendor(compTypeId, domainId, techId) {
        return this.http.get(this.API_URL + "competenceController/getVendor/" + compTypeId + "/" + domainId  + "/" + techId);
    }

    getDomain(compTypeId) {
        return this.http.get(this.API_URL + "competenceController/getDomain/" + compTypeId);
    }

    getServiceArea(compTypeId, domainId, techId, vendorId) {
        return this.http.get(this.API_URL + "competenceController/getServiceArea/" + compTypeId + "/" + domainId  + "/" + techId + "/" + vendorId);
    }

    getCompetence() {
        return this.http.get(this.API_URL + "competenceController/getCompetence");
    }

    getAmbition(gradeW, levelFlag) {
        return this.http.get(this.API_URL + "competenceController/getAmbition/" + levelFlag + '/' +gradeW);
    }

    getSavedCompetence(dataForComp){
        
        
        return this.http.post(this.API_URL + "competenceController/getUserCompetenceData", dataForComp);
    }

    getCompValuesFromApi(id){
        return this.http.get(this.API_URL + "competenceController/getUserCompetenceRow/" + id);
    }


    addCompetence(competenceData) {       

        return this.http.post(this.API_URL + "competenceController/insertCompetenceData", competenceData);
    }

}