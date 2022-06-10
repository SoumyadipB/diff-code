package com.ericsson.isf.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.AspManagementDao;
import com.ericsson.isf.dao.CountriesDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AspAcceptRejectModel;
import com.ericsson.isf.model.AspExplorerModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.AspVendorModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.Signum;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AspVendorColumnsEnum;
import com.ericsson.isf.util.FileUtil;

@Service
public class AspManagementService {

	private static final String INVALID_RECORDS_COUNT_MESSAGE = "Total invalid records count: ";
	private static final String INVALID_RECORDS_MESSAGE = "; Invalid rows are: ";
	private static final String MANAGER_SIGNUM = "managerSignum";
	private static final String EMPTY_STRING = "";
	private static final String DECIMAL_REGEX = "\\.0*$";
	public static final String START_DATE = "accessStartDate";
	public static final String END_DATE = "accessEndDate";
	public static final String STATUS_LOCKED = "LOCKED";
	public static final String STATUS_REJECTED = "REJECTED";
	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_IN_ACTIVE = "INACTIVE";
	public static final String STATUS_EXPIRED = "EXPIRED/REVOKED";
	public static final String STATUS_APPROVED = "APRROVED";
	public static final String STATUS_PENDING = "PENDING";
	public static final String ACTION_COMPLETED_ACCEPTED = "Approved Successfully";
	public static final String ACTION_COMPLETED_REJECTED = "Rejected Successfully";
	public static final String ACTION_COMPLETED_REVOKED = "Revoked Successfully";
	public static final String ACTION_COMPLETED_EXTENDED = "Extended Successfully";
	public static final String OPERATION_ACCEPTED = "ACCEPTED";
	public static final String OPERATION_REJECTED = "REJECTED";
	public static final String OPERATION_REVOKED = "REVOKED";
	public static final String OPERATION_EXTENDED = "EXTENDED";
	public static final String INSERT_SUCCESSFUL = "Vendor Details inserted successfully";
	public static final String INSERT_UNSUCCESSFUL = "Vendor Code is already present. Please try with different Vendor Code.";
	public static final String UPDATE_SUCCESSFUL = "Vendor Details updated successfully";
	public static final String ENABLE_SUCCESSFUL = "Vendor enabled successfully";
	public static final String DISABLE_SUCCESSFUL = "Vendor disabled successfully";

	@Autowired
	private AspManagementDao aspDao;

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private OutlookAndEmailService emailService;
	
	@Autowired
    private AccessManagementDAO accessManagementDAO;
	
	@Autowired
    private CountriesDao countriesDao;

	public AspVendorModel getVendorDetailsByID(String vendorCode) {
		return this.aspDao.getVendorDetailsByID(vendorCode);
	}

	public int getFailedAttempts(String signum) {
		return aspDao.getFailedAttempts(signum);
	}

	public void updateFailedAttempts(String signum, Integer failedAttempts) {
		aspDao.updateFailedAttempts(signum, failedAttempts);
	}

	public int isResetRequired(String signum) {
		return aspDao.isResetRequired(signum);
	}

	public void setResetRequired(Integer flag, String signum) {
		aspDao.setResetRequired(flag, signum);
	}

	public String getAspSignumByEmail(String email) {
		return aspDao.getAspSignumByEmail(email);
	}

	public int isProfileLocked(String signum) {
		return aspDao.isProfileLocked(signum);
	}

	public void setProfileLocked(Integer flag, String signum) {
		aspDao.setProfileLocked(flag, signum);
	}

	public int isProfileActive(String signum) {
		return aspDao.isProfileActive(signum);
	}

	public void setProfileActive(Integer flag, String signum) {
		aspDao.setProfileActive(flag, signum);
	}

	public void insertIntoUserAccessAsp(String signum) {
		aspDao.insertUserAccessAsp(signum);
	}

	public String getVendorByEmail(String email) {
		return aspDao.getVendorByEmail(email);
	}

	public AspLoginModel getAspDetailsBySignum(String signum) {
		return aspDao.getAspDetailsBySignum(signum);
	}

	public void resetFlagsWhenForgetPassword(String signum) {
		aspDao.resetFlagsWhenForgetPassword(signum);
	}

	public int isExpired(String signum) {
		Map<String, Date> dates = aspDao.isExpired(signum);
		Date startDate = dates.get(START_DATE);
		Date endDate = dates.get(END_DATE);
		Date currentDate = new Date();
		int flag;
		if (startDate.compareTo(currentDate) >= 0 && endDate.compareTo(currentDate) <= 0) {
			flag = 1;
		} else if (endDate.compareTo(currentDate) > 0 && startDate.compareTo(currentDate) >= 0) {
			flag = 2;
		} else {
			flag = 3;
		}
		return flag;
	}

	public List<AspExplorerModel> getAspExplorerForManager(String managerSignum) {
		List<AspExplorerModel> entries = aspDao.getAspExplorerForManager(managerSignum);
		for (AspExplorerModel entry : entries) {
			entry.setStatus(computeStatus(entry.getIsLocked(), entry.getIsActive(), entry.getIsProfileActive(),
					entry.getStartDate(), entry.getEndDate()));
		}
		return entries;
	}

	public String computeStatus(Boolean isLocked, Boolean isActive, Boolean isProfileActive, Date startDate,
			Date endDate) {
		String status;
		Date currentDate = new Date();
		if (startDate != null && endDate != null) {
			if (isProfileActive && !isLocked && (currentDate.after(startDate) || currentDate.equals(startDate))
					&& currentDate.before(endDate)) {
				status = STATUS_ACTIVE;
			} else if (isProfileActive && isLocked) {
				status = STATUS_LOCKED;
			} else if (currentDate.before(startDate)) {
				status = STATUS_APPROVED;
			} else {
				status = STATUS_EXPIRED;
			}
		} else {
			if (isActive) {
				status = STATUS_PENDING;
			} else {
				status = STATUS_REJECTED;
			}
		}
		return status;
	}

	public Response<Void> updateAspProfileAccess(AspAcceptRejectModel aspAcceptRejectModel) {
		Response<Void> response = new Response<>();
		if (aspAcceptRejectModel.getOperation().equalsIgnoreCase(OPERATION_ACCEPTED)) {
			aspDao.updateStatusInEmployeeTable(aspAcceptRejectModel.getUserSignum(), STATUS_ACTIVE);
			aspAcceptRejectModel.setIsProfileActiveFlag(true);
			aspAcceptRejectModel.setIsActiveFlag(true);
			response.addFormMessage(ACTION_COMPLETED_ACCEPTED);
			emailService.sendMail(AppConstants.ISF_ASP_APPROVE_REJECT,
					enrichMailforaspApprove(aspAcceptRejectModel, ACTION_COMPLETED_ACCEPTED, EMPTY_STRING));

		} else if (aspAcceptRejectModel.getOperation().equalsIgnoreCase(OPERATION_REJECTED)) {
			aspDao.updateStatusInEmployeeTable(aspAcceptRejectModel.getUserSignum(), STATUS_IN_ACTIVE);
			aspAcceptRejectModel.setIsProfileActiveFlag(false);
			aspAcceptRejectModel.setIsActiveFlag(false);
			response.addFormMessage(ACTION_COMPLETED_REJECTED);
			emailService.sendMail(AppConstants.ISF_ASP_APPROVE_REJECT, enrichMailforaspApprove(aspAcceptRejectModel,
					ACTION_COMPLETED_REJECTED, aspAcceptRejectModel.getRejectMessage()));

		} else if (OPERATION_EXTENDED.equalsIgnoreCase(aspAcceptRejectModel.getOperation())) {
			aspDao.updateStatusInEmployeeTable(aspAcceptRejectModel.getUserSignum(), STATUS_ACTIVE);
			aspAcceptRejectModel.setIsProfileActiveFlag(true);
			aspAcceptRejectModel.setIsActiveFlag(true);
			aspAcceptRejectModel.setStartDate(aspAcceptRejectModel.getStartDate());
			aspAcceptRejectModel.setEndDate(aspAcceptRejectModel.getEndDate());
			response.addFormMessage(ACTION_COMPLETED_EXTENDED);
			emailService.sendMail(AppConstants.ISF_ASP_EXTENDED, enrichMailforaspApprove(aspAcceptRejectModel,
					OPERATION_EXTENDED, aspAcceptRejectModel.getRejectMessage()));

		} else if (OPERATION_REVOKED.equalsIgnoreCase(aspAcceptRejectModel.getOperation())) {
			aspDao.updateStatusInEmployeeTable(aspAcceptRejectModel.getUserSignum(), STATUS_IN_ACTIVE);
			aspAcceptRejectModel.setIsProfileActiveFlag(false);
			aspAcceptRejectModel.setIsActiveFlag(false);
			response.addFormMessage(ACTION_COMPLETED_REVOKED);
			emailService.sendMail(AppConstants.ISF_ASP_REVOKED, enrichMailforaspApprove(aspAcceptRejectModel,
					ACTION_COMPLETED_REVOKED, aspAcceptRejectModel.getRejectMessage()));

		}
		aspDao.updateAspProfileAccess(aspAcceptRejectModel);
		return response;
	}

	public AspExplorerModel getAspFlags(String signum) {
		return aspDao.getAspFlags(signum);
	}

	public static final String ASP_EMPLOYEE_EMAIL = "EmployeeEmailID";

	public Map<String, Object> enrichMailforaspApprove(AspAcceptRejectModel req, String status, String comment) {
		Map<String, Object> data = new HashMap<>();
		AspLoginModel aspData = aspDao.getAspDetailsBySignum(req.getUserSignum());
		EmployeeModel managerData = activityMasterDAO.getEmployeeBySignum(req.getManagerSignum());
		data.put("status", status);
		data.put("managerName", managerData.getEmployeeName());
		data.put(MANAGER_SIGNUM, req.getManagerSignum());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, aspData.getEmail());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, managerData.getEmployeeEmailId());
		data.put("startDate", req.getStartDate());
		data.put("endDate", req.getEndDate());
		data.put("aspData", aspData);
		data.put("comment", comment);
		return data;
	}

	public List<AspVendorModel> getAllVendors() {
		return aspDao.getAllVendors();
	}

	public List<Map<String, Object>> getAspByVendor(String vendorCode) {
		return aspDao.getAspByVendor(vendorCode);
	}

	public List<Map<String, Object>> getAspByScope(String projectScopeId) {
		String vendorCode = aspDao.getVendorByScope(projectScopeId);
		return aspDao.getAspByVendor(vendorCode);
	}

	public List<Map<String, Object>> getAspByWoid(String projectScopeId) {
		String vendorCode = aspDao.getVendorByWoid(projectScopeId);
		return aspDao.getAspByVendor(vendorCode);
	}

	public void updateAspLastPasswordChange(String signum) {
		aspDao.updateAspLastPasswordChange(signum);
	}

	public List<AspVendorModel> getAllAspVendorDetails() {
		return aspDao.getAllAspVendorDetails();
	}

	public Response<Void> insertAspVendorDetails(AspVendorModel input) {
		Response<Void> response = new Response<>();
		if (aspDao.getVendorDetailsByID(input.getVendorCode()) == null) {
			aspDao.insertAspVendorDetails(input);
			response.addFormMessage(INSERT_SUCCESSFUL);

		} else {
			response.addFormError(INSERT_UNSUCCESSFUL);
		}

		return response;
	}

	public Response<Void> updateAspVendorDetails(AspVendorModel input) {
		Response<Void> response = new Response<>();
		aspDao.updateAspVendorDetails(input);
		response.addFormMessage(UPDATE_SUCCESSFUL);		
		return response;
	}
	

	public Response<Void> enableDisableAspVendorDetails(String vendorCode, String signum) {
		Response<Void> response = new Response<>();
		try {
			if (aspDao.getAllAspVendorModelByCode(vendorCode).getIsActive() == 1) {
				aspDao.enableDisableAspVendorDetails(vendorCode, signum, 0);
				response.addFormMessage(DISABLE_SUCCESSFUL);
			} else {
				aspDao.enableDisableAspVendorDetails(vendorCode, signum, 1);
				response.addFormMessage(ENABLE_SUCCESSFUL);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public Response<Void> insertAspVendorDetailsFromFile(MultipartFile file) {
		return parseFileIntoModel(file);
	}
	
	private Row getCurrentRow(Sheet sheet, int rowNo, MultipartFile file, int actualRowNumber, List<Integer> invalidRows) {
		Row currentRow=FileUtil.getRow(sheet, 0, file);
		try {
			currentRow = FileUtil.getRow(sheet, rowNo, file);
		} catch (ApplicationException e) {
			invalidRows.add(actualRowNumber);
		}
		return currentRow;
	}
	private boolean setManagerSignumInAspVendorModel(String cellValue, AspVendorModel aspVendorModel, List<Integer> invalidRows, int actualRowNumber) {
		boolean isRowValidForInsertion=true;
		try {
			Signum signum = new Signum(cellValue);
			if (aspDao.isSignumASPM(signum.getSignum())) {
				aspVendorModel.setManagerSignum(signum.getSignum());
			} else {
				invalidRows.add(actualRowNumber);
				isRowValidForInsertion = false;
			}
		} catch (ApplicationException e) {
			invalidRows.add(actualRowNumber);
			isRowValidForInsertion = false;
		}
		return isRowValidForInsertion;
	}
	private Response<Void> parseFileIntoModel(MultipartFile file) {
		Response<Void> response = new Response<>();
		try {
			// Throws ApplicationException if valid file not found
			Workbook workbook = FileUtil.getWorkBookFromFile(file);
			Sheet sheet = workbook.getSheetAt(0);
			Row currentRow;
			String cellValue;

			// Throws ApplicationException if sheet is empty
			int rows = FileUtil.getNumberofRows(sheet, file);
			boolean isRowValidForInsertion;
			int actualRowNumber;
			List<Integer> validRows = new LinkedList<>();
			List<Integer> invalidRows = new LinkedList<>();

			currentRow = FileUtil.getRow(sheet, 0, file);
			int totalColumnNumber = currentRow.getLastCellNum();

			// Check column sequence or empty header value
			boolean isColumnSequence = FileUtil.isColumnInSequence(AspVendorColumnsEnum.getAllColumnsForAspVendor(),
					currentRow);
			if (!isColumnSequence) {
				throw new ApplicationException(500, "Error found in sheet:'" + sheet.getSheetName()
						+ "' header, check sequence of columns as per template file or any empty header value.");
			}

			for (int rowNo = 1; rowNo < rows; rowNo++) {

				actualRowNumber = rowNo + 1;
				currentRow=getCurrentRow(sheet, rowNo, file, actualRowNumber, invalidRows);

				if (totalColumnNumber < currentRow.getLastCellNum()) {
					invalidRows.add(actualRowNumber);
					continue;
				}

				isRowValidForInsertion = true;
				AspVendorModel aspVendorModel = new AspVendorModel();

				for (int colNo = 0; (colNo < totalColumnNumber) && (isRowValidForInsertion); colNo++) {
					cellValue = currentRow.getCell(colNo, currentRow.CREATE_NULL_AS_BLANK).toString().trim();
					if (colNo != 3 && StringUtils.isBlank(cellValue)) {
						invalidRows.add(actualRowNumber);
						isRowValidForInsertion = false;
						break;
					}
					switch (colNo) {
					case 0:
						aspVendorModel.setVendorCode(cellValue.replaceAll(DECIMAL_REGEX, EMPTY_STRING));
						break;
					case 1:
						if (countriesDao.isCountryExists(cellValue)) {
							aspVendorModel.setCountry(cellValue);
						} else {
							invalidRows.add(actualRowNumber);
							isRowValidForInsertion = false;
						}
						break;
					case 2:
						aspVendorModel.setVendorName(cellValue);
						break;
					case 3:
						if (StringUtils.isNotBlank(cellValue)) {
							aspVendorModel.setVendorContactDetails(cellValue.replaceAll(DECIMAL_REGEX, EMPTY_STRING));
						}
						break;
					case 4:
						isRowValidForInsertion=setManagerSignumInAspVendorModel(cellValue, aspVendorModel, invalidRows, actualRowNumber);						
						break;
					default:
						invalidRows.add(actualRowNumber);
						isRowValidForInsertion = false;
						break;

					}
				}

				if (isRowValidForInsertion) {
					if (aspDao.getAllAspVendorModelByCode(aspVendorModel.getVendorCode()) == null) {
						aspDao.insertAspVendorDetails(aspVendorModel);
						validRows.add(actualRowNumber);
					} else {
						invalidRows.add(actualRowNumber);
					}
				}
			}

			if (!validRows.isEmpty()) {
				response.addFormMessage(INSERT_SUCCESSFUL + "; Total success records count: " + validRows.size());
			}
			if (!invalidRows.isEmpty()) {
				response.addFormError(INVALID_RECORDS_COUNT_MESSAGE + invalidRows.size() + INVALID_RECORDS_MESSAGE
						+ invalidRows.toString());
			}
		} catch (ApplicationException | IOException e) {
			response.addFormError(e.getMessage());
		} catch (POIXMLException e) {
			response.addFormError("Invalid file format: " + file.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
			response.addFormError(e.getMessage());
		}
		return response;
	}

	public List<AspVendorModel> getAllActiveAspVendors() {
		return aspDao.getAllActiveAspVendors();
	}
}
