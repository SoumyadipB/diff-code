export const environment = {
    production: false,
    authConfig: {
      instance: 'https://login.microsoftonline.com/',
      tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      clientId: 'dc8ac1ff-71ea-4b38-b000-918c89911ea9',
      redirectUri: 'https://integratedserviceflow.ericsson.net/Competence_PROD_DR/',
      navigateToLoginRequestUrl: false,
      cacheLocation: 'localStorage'
    },
    GLOBAL_API_URL : "https://preprod.integratedserviceflow.ericsson.net/isf-rest-server-java_PROD/",
   rootDir:"/Competence_PROD_DR/",
   logoutRedirect:"https://integratedserviceflow.ericsson.net/",
   LMTestingEmail:""
  };
  