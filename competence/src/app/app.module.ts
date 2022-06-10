import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatModule } from './mat-module'; 
 
// import { LayoutModule } from '@angular/cdk/layout';
import { navModule } from './nav/nav.module';
import { WelcomeComponent } from './welcome/welcome.component';
import { LoginComponent } from './auth/login/login.component';
import { FormsModule } from '@angular/forms';
import {ReactiveFormsModule} from '@angular/forms';
import { MyCompetenceModule } from './my-competence/my-competence.module';
import {CompetenceRequestComponent} from './competence-request/competence-request.component'
import {EngineerRequestComponent} from './competence-request/engineer-request/engineer-request.component'
import { AuthInterceptor } from './shared/auth.interceptor';
import {EngineerComponent} from './competence-request/engineer/engineer.component'
import { AuthenticationGuard, MsAdalAngular6Module } from 'microsoft-adal-angular6';
import { environment } from './../environments/environment';
import { CompetenceAlertComponent } from './shared/competence-alert/competence-alert.component';
import { RefreshPageComponent } from './refresh-page/refresh-page.component';
import { CompetenceProgressLMComponent } from './competence-request/competence-progress-lm/competence-progress-lm.component';
import { AlertBoxComponent } from './shared/alert-box/alert-box.component';
import { ConnectionAlertComponent } from './shared/connection-alert/connection-alert.component';
import { EditCompetenceProgressComponent } from './competence-request/edit-competence-progress/edit-competence-progress.component';
import { OnlyNumberDirective } from './shared/Custom Directives/onlyNumber/only-number.directive';
import { FlexLayoutModule } from "@angular/flex-layout";
import { MatTableExporterModule } from 'mat-table-exporter'



@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent, 
    LoginComponent,
    CompetenceRequestComponent,
    EngineerRequestComponent,
    EngineerComponent,
    CompetenceAlertComponent,
    RefreshPageComponent,
    CompetenceProgressLMComponent,
    AlertBoxComponent,
    ConnectionAlertComponent,
    EditCompetenceProgressComponent,
    OnlyNumberDirective ,
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    // LayoutModule,
    navModule ,        
    FormsModule,    
    ReactiveFormsModule,
    HttpClientModule ,
    MatModule,
    MyCompetenceModule,
    MsAdalAngular6Module.forRoot(environment.authConfig),
    FlexLayoutModule,
    MatTableExporterModule
    
  ],
  providers: [AuthenticationGuard,
    {provide:HTTP_INTERCEPTORS,useClass:AuthInterceptor,multi:true}
  ],
  bootstrap: [AppComponent],
  entryComponents:[EngineerRequestComponent,EngineerComponent, CompetenceAlertComponent, AlertBoxComponent, ConnectionAlertComponent, EditCompetenceProgressComponent]
})
export class AppModule { }
