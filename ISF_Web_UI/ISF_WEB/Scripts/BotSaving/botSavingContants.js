
// BOT SAVING REMARKS
const C_ZERO_SAVINGS = `A Zero EME was calculated by ISF on `;
const C_SAVINGS_AS_EXPECTED_SAVINGS = "No calculation done as no step was removed, Expected saving is saved.";
const C_SAVINGS_TO_BE_CALCULATED = "New manual step introduced. Saving will be reported after enough records generated.";
const C_SAVINGS_CALC_IN_RANGE = "Saving calculated in range as per user Expected Savings.";
const C_MIN_WO_CONDITION_FAIL = "The minimum WOs were not found. Calculated with available data. Recheck parent mapping.";
const C_SAVINGS_CALC_NOT_IN_RANGE = 'Saving calculated is not in range as per user Expected Savings.</br> User have 3 options:</br>' +
    '1. User can save WF with same savings.</br>' +
    '2. User can change the WorkFlow and recalculate the savings.</br>' +
    '3. User can change the step mapping and recalculate.';
const C_SAVE_CALC_SAVINGS_NOT_IN_RANGE = "Saving calculated is not in range as per user Expected Savings. Calculated Savings saved.";
const C_NO_SAVINGS_AVAILABLE = "No calculation done as the savings not available, Expected saving is saved";
const C_NO_SAVINGS_AVAILABLE_ALERT = 'Savings not calculated as 20 WOs are not executed.</br> User have 2 options:</br>' +
    '1. User can save WF with expected savings.</br>' +
    '2. User can change the expected savings and recalculate the savings.</br>' +
    'User will be able to choose mapping and recalculate savings in future';
const C_NO_SAVINGS_AVAILABLE_FOR_MANUAL = "No calculation done as the savings not available for manual step/steps, Calculated with available data. Recheck parent mapping.";
//new constants for new remarks
const C_ZERO_SAVINGS_WITH_NEW_WF = "New automation with New WF.User saved Zero EME.EME Change Request to be raised by";
const C_EME_CALCULATED = `The EME was calculated by ISF on ${currentDateInFormatYMD}, by ${signumGlobal}`;
const C_RECALCULATE_EME = "The EME was recalculated, by ISF,  as requested by";
// OTHER CONSTANTS
const C_ERR_EXPECTED_SAVINGS = "Please enter Expected Savings";
const C_TO_BE_CALCULATED = "To be Calculated";
const C_BOTSAVING = "botSaving";
const C_NA = "NA";

//The EME includes the EME of <RPAID>, which was replaced on <current date>,by <current user>
const C_PARTIAL_SAVINGS_TO_NEW_AUTO_STEP1 = `The EME includes the EME of`;
const C_PARTIAL_SAVINGS_TO_NEW_AUTO_STEP2 = `, which was replaced on ${currentDateInFormatYMD}, by ${signumGlobal}`;
