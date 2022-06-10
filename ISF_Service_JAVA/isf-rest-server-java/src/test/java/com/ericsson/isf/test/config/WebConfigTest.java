package com.ericsson.isf.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ericsson.isf.azure.keyVault.service.ClientSecretKeyValueProvider;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.config.AzureConfigurations;
import com.ericsson.isf.controller.WOExecutionController;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.AdhocActivityDao;
import com.ericsson.isf.dao.AppUtilDAO;
import com.ericsson.isf.dao.AspManagementDao;
import com.ericsson.isf.dao.AuditManagementDAO;
import com.ericsson.isf.dao.AzureDAO;
import com.ericsson.isf.dao.BotStoreDao;
import com.ericsson.isf.dao.CountriesDao;
import com.ericsson.isf.dao.DemandForecastDAO;
import com.ericsson.isf.dao.DemandManagementDAO;
import com.ericsson.isf.dao.EnvironmentPropertyDao;
import com.ericsson.isf.dao.ExternalInterfaceManagmentDAO;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.IsfCustomIdDao;
import com.ericsson.isf.dao.ProjectDAO;
import com.ericsson.isf.dao.ProjectScopeDao;
import com.ericsson.isf.dao.ProjectScopeDetailDao;
import com.ericsson.isf.dao.ResourceRequestDAO;
import com.ericsson.isf.dao.RpaDAO;
import com.ericsson.isf.dao.ToolsMasterDAO;
import com.ericsson.isf.dao.WorkOrderPlanDao;
import com.ericsson.isf.dao.impl.AdhocActivityDaoImpl;
import com.ericsson.isf.dao.impl.BotStoreDaoImpl;
import com.ericsson.isf.jwtSecurity.DemoInterceptor;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.service.ActivityMasterService;
import com.ericsson.isf.service.AdhocActivityService;
import com.ericsson.isf.service.AppService;
import com.ericsson.isf.service.AspManagementService;
import com.ericsson.isf.service.AzureService;
import com.ericsson.isf.service.BotService;
import com.ericsson.isf.service.BotStoreService;
import com.ericsson.isf.service.DemandForecastService;
import com.ericsson.isf.service.DemandManagementService;
import com.ericsson.isf.service.EnvironmentPropertyService;
import com.ericsson.isf.service.ExternalInterfaceManagmentService;
import com.ericsson.isf.service.FlowChartService;
import com.ericsson.isf.service.IsfCustomIdService;
import com.ericsson.isf.service.JdbcConnectionFactory;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.service.ProjectService;
import com.ericsson.isf.service.RpaService;
import com.ericsson.isf.service.ToolsMasterService;
import com.ericsson.isf.service.ValidationUtilityService;
import com.ericsson.isf.service.WOExecutionService;
import com.ericsson.isf.service.WorkOrderPlanService;
import com.ericsson.isf.service.audit.AuditDataProcessor;
import com.ericsson.isf.service.audit.AuditManagementService;
import com.ericsson.isf.service.audit.AuditManager;
import com.ericsson.isf.service.impl.AdhocActivityServiceImpl;
import com.ericsson.isf.service.impl.BotStoreServiceImpl;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.ericsson.isf.util.MailUtil;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

/**
 * Web config test class responsible to initialize beans
 * 
 * @author eakinhm
 *
 */

@EnableScheduling
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.ericsson.isf.service", "com.ericsson.isf.dao", "com.ericsson.isf.test.config" })
@EnableTransactionManagement(proxyTargetClass = true)
public class WebConfigTest extends WebMvcConfigurerAdapter {
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/", "CLASSPATH:/resources/");
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"));
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		return resolver;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
	}

	@Bean
	DemoInterceptor demoInterceptor() {
		return new DemoInterceptor();
	}
	
	@Bean
	public WOExecutionController woExecutionController() {
		return new WOExecutionController();
	}
	
	@Bean
	public ApplicationConfigurations configurations() {
		return new ApplicationConfigurations();
	}
	
	@Bean
	public AzureConfigurations azureConfigurations() {
		return new AzureConfigurations();
	}
	
	@Bean
	public AspManagementDao aspManagementDao() {
		return new AspManagementDao();
	}

	@Bean
	public AspManagementService aspManagementService() {
		return new AspManagementService();
	}
	
	@Bean
	public ActivityMasterDAO activityMasterDAO() {
		return new ActivityMasterDAO();
	}
	
	@Bean
	public OutlookAndEmailService emailService() {
		return new OutlookAndEmailService();
	}
	
	@Bean
	public AdhocActivityService adhocActivityService() {
		return new AdhocActivityServiceImpl();
	}
	
	@Bean
	public AdhocActivityDao adhocActivityDao() {
		return new AdhocActivityDaoImpl();
	}
	
	@Bean
	public WOExecutionService woExecutionService() {
		return new WOExecutionService();
	}
	
	@Bean
	public IsfCustomIdInsert isfCustomIdInsert() {
		return new IsfCustomIdInsert();
	}
	
	@Bean
	public IsfCustomIdService isfCustomIdService() {
		return new IsfCustomIdService();
	}
	
	@Bean
	public IsfCustomIdDao isfCustomIdDao() {
		return new IsfCustomIdDao();
	}
	
	@Bean
	public WorkOrderPlanService woPlanService() {
		return new WorkOrderPlanService();
	}
	
	@Bean
	public WorkOrderPlanDao workOrderPlanDao() {
		return new WorkOrderPlanDao();
	}
	
	@Bean
	public EnvironmentPropertyService environmentPropertyService() {
		return new EnvironmentPropertyService();
	}
	
	@Bean
	public EnvironmentPropertyDao environmentPropertyDao() {
		return new EnvironmentPropertyDao();
	}
	

	@Bean
	public ExternalInterfaceManagmentDAO externalInterfaceManagmentDAO() {
		return new ExternalInterfaceManagmentDAO();
	}
	
	@Bean
	public ProjectScopeDao projectScopeDao() {
		return new ProjectScopeDao();
	}
	
	@Bean
	public FlowChartDAO flowChartDao() {
		return new FlowChartDAO();
	}
	
	@Bean
	public AuditManager auditManager() {
		return new AuditManager();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		return new ThreadPoolTaskExecutor();
	}
	
	@Bean
	public BotStoreService botStoreService() {
		return new BotStoreServiceImpl();
	}
	
	@Bean
	public BotStoreDao botStoreDAO() {
		return new BotStoreDaoImpl();
	}
	
	@Bean
	public RpaService rpaService() {
		return new RpaService();
	}
	
	@Bean
	public RpaDAO rpaDAO() {
		return new RpaDAO();
	}
	
	@Bean
	public ProjectService projectService() {
		return new ProjectService();
	}
	
	@Bean
	public ProjectDAO projectDAO() {
		return new ProjectDAO();
	}
	
	@Bean
	public ProjectScopeDetailDao projectScopeDetailDao() {
		return new ProjectScopeDetailDao();
	}
	
	@Bean
	public AccessManagementDAO accessManagementDAO() {
		return new AccessManagementDAO();
	}
	
	@Bean
	public ActivityMasterService activityMasterService() {
		return new ActivityMasterService();
	}
	
	@Bean
	public FlowChartService flowChartService() {
		return new FlowChartService();
	}
	
	@Bean
	public ToolsMasterService toolsMasterService() {
		return new ToolsMasterService();
	}
	
	@Bean
	public ToolsMasterDAO toolsMasterDAO() {
		return new ToolsMasterDAO();
	}
	
	@Bean
	public MailUtil mailUtil() {
		return new MailUtil();
	}
	
	@Bean
	public AppService appService() {
		return new AppService();
	}
	
	@Bean
	public JdbcConnectionFactory jdbcConnectionFactory() {
		return new JdbcConnectionFactory();
	}
	
	@Bean
	public DemandForecastService demandForecastService() {
		return new DemandForecastService();
	}
	
	@Bean
	public DemandForecastDAO demandForecastDAO() {
		return new DemandForecastDAO();
	}
	
	@Bean
	public DemandManagementService demandManagementService() {
		return new DemandManagementService();
	}
	
	@Bean
	public DemandManagementDAO demandManagementDAO() {
		return new DemandManagementDAO();
	}
	
	@Bean
	public AuditManagementService auditServiec() {
		return new AuditManagementService();
	}
	
	@Bean
	public AuditManagementDAO auditManagementDAO() {
		return new AuditManagementDAO();
	}
	
	@Bean
	public AccessManagementService accessManagementService() {
		return new AccessManagementService();
	}
	
	@Bean
	public ValidationUtilityService validationUtilityService() {
		return new ValidationUtilityService();
	}
	
	@Bean
	public ExternalInterfaceManagmentService externalInterfaceManagmentService() {
		return new ExternalInterfaceManagmentService();
	}
	
	@Bean
	public AuditDataProcessor auditDataProcessor() {
		return new AuditDataProcessor();
	}
	
	@Bean
	public BotService botService() {
		return new BotService();
	}
	
	@Bean
	public CloudBlobClient cloudBlobClient() throws Exception {
		return azureConfigurations().cloudBlobClient();
	}
	
	@Bean
	public ClientSecretKeyValueProvider clientSecretKeyValueProvider() {
		return new ClientSecretKeyValueProvider();
	}
	
	@Bean
	public AzureService azureService() {
		return new AzureService();
	}
	
	@Bean
	public AzureDAO azureDAO() {
		return new AzureDAO();
	}
	
	@Bean
	public CountriesDao countriesDao() {
		return new CountriesDao();
	}
	
	@Bean
	public CloudBlobContainer cloudBlobContainer() throws Exception {
		return azureConfigurations().testBlobContainer();
	}
	
	@Bean
	public AppUtilDAO appUtilDAO() {
		return new AppUtilDAO();
	}
	
	@Bean
	public ResourceRequestDAO resourceRequestDAO() {
		return new ResourceRequestDAO();
	}
	
	
	  @Bean 
	  public CloudBlobContainer commonBlobContainer() throws Exception {
	  return azureConfigurations().commonBlobContainer(); }
	 
}