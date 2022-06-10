export const environment = {
  production: false,
  authConfig: {
    instance: 'https://login.microsoftonline.com/',
    tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    clientId: 'ac92e7a6-a070-4a20-921e-3d1ab6f06908',
    redirectUri: 'https://isf.internal.ericsson.com/Competence_PROD',
    navigateToLoginRequestUrl: false,
    cacheLocation: 'localStorage'
  },
  GLOBAL_API_URL : "https://isfservices-lb.internal.ericsson.com:8443/isf-rest-server-java_PROD/",
 rootDir:"/Competence_PROD/",
 logoutRedirect:"https://isf.internal.ericsson.com/",
 LMTestingEmail:""
};
