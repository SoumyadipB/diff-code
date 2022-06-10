export const environment = {
    production: false,
    authConfig: {
      instance: 'https://login.microsoftonline.com/',
      tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      clientId: '37fc3a4d-61b4-4c90-8c41-c5de43fb1916',
      redirectUri: 'https://preprod.integratedserviceflow.ericsson.net/UI_DR_Competence/',
      navigateToLoginRequestUrl: false,
      cacheLocation: 'localStorage'
    },
    GLOBAL_API_URL : "https://preprod.integratedserviceflow.ericsson.net/isf-rest-server-java_DR/",
   rootDir:"/UI_DR_Competence/",
   logoutRedirect:"https://non-prod.integratedserviceflow.ericsson.net/Preprod_UI/",
   LMTestingEmail:""
  };
  