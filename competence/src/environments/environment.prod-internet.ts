export const environment = {
    production: false,
    authConfig: {
      instance: 'https://login.microsoftonline.com/',
      tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
      clientId: '71832908-c2f5-4494-a2e4-cf002dbbfeb7',
      redirectUri: 'https://isf.ericsson.net/Competence_PROD',
      navigateToLoginRequestUrl: false,
      cacheLocation: 'localStorage'
    },
    GLOBAL_API_URL : "https://isf.ericsson.net/IsfProxy/Data?param=",
   rootDir:"/Competence_PROD/",
   logoutRedirect:"https://isf.ericsson.net/",
   LMTestingEmail:""
  };
  