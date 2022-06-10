import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { Subject, BehaviorSubject } from "rxjs";

import { environment } from 'src/environments/environment';
import { AuthData } from "./auth-data.module";

@Injectable({ providedIn: "root" })
export class AuthService {
  private isAuthenticated = false;
  private token: string;
  private tokenTimer: any;
  private authStatusListener = new Subject<boolean>();

  apiUrl = environment.GLOBAL_API_URL;

  constructor(private http: HttpClient, private router: Router) { }

  private UserName = new BehaviorSubject<string>(atob(localStorage.getItem('empName')));
  private UserSignum = new BehaviorSubject<string>(atob(localStorage.getItem('loggedIn')));
  private UserRole = new BehaviorSubject<string>(atob(localStorage.getItem('userRole')));
  private accessToken = new BehaviorSubject<string>(localStorage.getItem('token'));


  getToken() {
    return this.token;
  }

  getIsAuth() {
    return this.isAuthenticated;
  }

  getAuthStatusListener() {
    return this.authStatusListener.asObservable();
  }

  createUser(email: string, password: string) {
    const authData: AuthData = { email: email, password: password };
    this.http
      .post("http://localhost:3000/api/user/signup", authData)
      .subscribe(response => {
        console.log(response);
      });
  }

  createToken(response) {
    const token = response.token;
    this.token = token;
    if (token) {
      const expiresInDuration = response.expiresIn;
      this.setAuthTimer(expiresInDuration);
      this.isAuthenticated = true;
      this.authStatusListener.next(true);
      const now = new Date();
      const expirationDate = new Date(now.getTime() + expiresInDuration * 1000);
      //console.log(expirationDate);
      //this.saveAuthData(token, expirationDate);
      //this.router.navigate(["/"]);
      return true;
    } else {
      return false;
    }


  }


  private getUserDataByEmail(email: string) {
    const passData = { employeeEmailID: email };

    return this.http
      .post<{ employeeEmailID: string; }>(
        this.apiUrl + "accessManagement/getUserProfileByEmail",
        passData
      );


  }


  loginAfterAdal(email: string) {

    if(environment.LMTestingEmail!=""){ // just for testing as a LM
      email=environment.LMTestingEmail;
    }
   //email = "yogesh.y.kumar@ericsson.com";
    //email="himanshu.rai@ericsson.com";
    //email="maninder.singh.walia@ericsson.com"
    this.getUserDataByEmail(email).subscribe(  // get user details
      (response) => {
        //console.log(response);
        const userRole = (response[0].isLineManager === "N") ? "User" : "LM";
        const signum = response[0].signumID;
        const emp = response[0].employeeName;
        const token =response[0].sessionToken;
        this.saveAuthData(userRole, signum, emp,token);


        if (userRole === "User") {
          this.router.navigate(["/competence"]);
        } else if(userRole === "LM"){
          this.router.navigate(["/competence-request"]);
        }else if(userRole === null){
          this.router.navigate(["/welcome"]);
        }

      }
    );
  }


  login(email: string, password: string) {
    const authData: AuthData = { email: email, password: password };
    return this.http
      .post<{ token: string; expiresIn: number }>(
        "http://localhost:3000/api/user/login",
        authData
      );

  }

  // autoAuthUser() {
  //   const authInformation = this.getAuthData();
  //   if (!authInformation) {
  //     return;
  //   }
  //   const now = new Date();
  //   const expiresIn = authInformation.expirationDate.getTime() - now.getTime();
  //   if (expiresIn > 0) {
  //     this.token = authInformation.token;
  //     this.isAuthenticated = true;
  //     this.setAuthTimer(expiresIn / 1000);
  //     this.authStatusListener.next(true);
  //   }
  // }

  // logout() {
  //   this.token = null;
  //   this.isAuthenticated = false;
  //   this.authStatusListener.next(false);
  //   clearTimeout(this.tokenTimer);
  //   this.clearAuthData();
  //   this.router.navigate(["/"]);
  // }

  logout() {
    this.clearAuthData();
  }

  private setAuthTimer(duration: number) {
    //console.log("Setting timer: " + duration);
    this.tokenTimer = setTimeout(() => {
      this.logout();
    }, duration * 1000);
  }

  // private saveAuthData(token: string, expirationDate: Date) {
  //   localStorage.setItem("token", token);
  //   localStorage.setItem("expiration", expirationDate.toISOString());
  // }

  private saveAuthData(userRole: string, loggedIn: string, empName: string,token: string) {
    // localStorage.setItem("userRole", userRole);
    // localStorage.setItem("loggedIn", loggedIn);
    // localStorage.setItem("empName", empName);

    localStorage.setItem("userRole", btoa(userRole));
    localStorage.setItem("loggedIn", btoa(loggedIn));
    localStorage.setItem("empName", btoa(empName));
    localStorage.setItem("token", token);
    
    this.UserName.next(atob(localStorage.getItem('empName')));
    this.UserSignum.next(atob(localStorage.getItem('loggedIn')));
    this.UserRole.next(atob(localStorage.getItem('userRole')));
    this.accessToken.next(localStorage.getItem('token'));

  }

  private clearAuthData() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("loggedIn");
    localStorage.removeItem("empName");
    localStorage.removeItem("token");
  }

  // private getAuthData() {
  //   const token = localStorage.getItem("token");
  //   const expirationDate = localStorage.getItem("expiration");
  //   if (!token || !expirationDate) {
  //     return;
  //   }
  //   return {
  //     token: token,
  //     expirationDate: new Date(expirationDate)
  //   }
  // }

  getAuthData() {
    
    let userRole = atob(localStorage.getItem("userRole"));
    let loggedInSignum = atob(localStorage.getItem("loggedIn"));
    let loggedInEmpName = atob(localStorage.getItem("empName"));
    let token = localStorage.getItem("token");


    //this.UserRole.next(atob(localStorage.getItem('userRole')));

    //this.UserRole.subscribe(res=>{userRole=res});
    // const userRole = localStorage.getItem("userRole");
    // const loggedInSignum = localStorage.getItem("loggedIn");
    // const loggedInEmpName = localStorage.getItem("empName");

    if (userRole && loggedInSignum) {
      //console.log("Auth IF");
      return {
        userRole: userRole,
        loggedInSignum: loggedInSignum,
        empName: loggedInEmpName,
        accessToken:token
      }
    } else {
      //console.log("Auth ELSE");
      return {
        userRole: "",
        loggedInSignum: "",
        empName: "",
        accessToken:""
      }
    }
  }


  
  get currentUserSignum() 
    {
        return this.UserSignum.asObservable();
    }

    get currentUserName() 
    {
        return this.UserName.asObservable();
    }

   get currentUserRole() 
    {
        return this.UserRole.asObservable();
    }

    get currentAccessToken() 
    {
        return this.accessToken.asObservable();
    }


}

