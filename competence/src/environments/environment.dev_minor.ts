export const environment = {
    production: false,
    authConfig: {
      instance: 'https://login.microsoftonline.com/',
      tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      clientId: '927839ae-96ad-4a1d-830d-c793212c4234',
      redirectUri: 'https://preprod.integratedserviceflow.ericsson.net/COMPETENCE_UI_DEV_MINOR/',
      navigateToLoginRequestUrl: false,
      cacheLocation: 'localStorage'
    },
    GLOBAL_API_URL : "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_minor/",
   rootDir:"/COMPETENCE_UI_DEV_MINOR/",
   logoutRedirect:"https://preprod.integratedserviceflow.ericsson.net/ISF_UI_DEV_MINOR/",
   LMTestingEmail:""
  };