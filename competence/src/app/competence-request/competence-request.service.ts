import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';



@Injectable({
    providedIn:"root"
})
export class CompetenceRequestService{
//API_URL="https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/";
API_URL = environment.GLOBAL_API_URL;
    //http: any;
constructor(private http: HttpClient){}

getCompetenceForApproval(data){

    return this.http.post(this.API_URL + "competenceController/getEngDataForLM",data);
}
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
getCompValuesFromApi(id){
    return this.http.get(this.API_URL + "competenceController/getUserCompetenceRow/" + id);
}
addCompetence(competenceData) {
    
    return this.http.post(this.API_URL + "competenceController/insertCompetenceData", competenceData);
}

getSavedCompetence(dataForComp){
        
        
    return this.http.post(this.API_URL + "competenceController/getUserCompetenceData", dataForComp);
}

updateCompetenceAssesmentData(data){

    return this.http.post(this.API_URL + "competenceController/updateCompetenceAssesmentData", data);
}

}