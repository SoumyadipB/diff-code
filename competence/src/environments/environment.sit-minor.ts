export const environment = {
    production: false,
    authConfig: {
      instance: 'https://login.microsoftonline.com/',
      tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      clientId: '37fc3a4d-61b4-4c90-8c41-c5de43fb1916',
      redirectUri: 'https://isfsit.internal.ericsson.com/COMPETENCE_UI_SIT_MAJOR/',
      navigateToLoginRequestUrl: false,
      cacheLocation: 'localStorage'
    },
    GLOBAL_API_URL : "https://isfsitservices.internal.ericsson.com:8443/isf-rest-server-java_sit_minor/",
    rootDir:"/COMPETENCE_UI_SIT_MAJOR/",
    logoutRedirect:"https://isfsit.internal.ericsson.com/ISF_UI_SIT_MINOR/",
    LMTestingEmail:""
  };