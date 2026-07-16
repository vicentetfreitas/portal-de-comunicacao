/**
 * Helpers compartilhados — Sprint API-VALIDATION-01
 * Copiar trechos para scripts de coleção/requisição no Postman ou importar via require (Postman sandbox).
 */

function assertApiResponseSuccess() {
    pm.test('HTTP 2xx', () => pm.response.to.be.success);
    const json = pm.response.json();
    pm.test('Envelope ApiResponse', () => {
        pm.expect(json).to.have.property('success', true);
        pm.expect(json).to.have.property('timestamp');
    });
    return json;
}

function assertErrorResponse(expectedStatus, expectedError) {
    pm.test('HTTP ' + expectedStatus, () => pm.response.to.have.status(expectedStatus));
    const json = pm.response.json();
    pm.test('Envelope ErrorResponse', () => {
        pm.expect(json).to.have.property('status', expectedStatus);
        pm.expect(json).to.have.property('error');
        pm.expect(json).to.have.property('message');
        pm.expect(json).to.have.property('path');
    });
    if (expectedError) {
        pm.test('error code ' + expectedError, () => pm.expect(json.error).to.eql(expectedError));
    }
    return json;
}

function assertPageResponse() {
    const json = assertApiResponseSuccess();
    pm.test('PageResponse shape', () => {
        pm.expect(json.data).to.have.property('content');
        pm.expect(json.data).to.have.property('page');
        pm.expect(json.data).to.have.property('size');
        pm.expect(json.data).to.have.property('totalElements');
        pm.expect(json.data).to.have.property('totalPages');
    });
    return json;
}

function saveIdFromCreate(varName) {
    const json = pm.response.json();
    if (json.data && json.data.id) {
        pm.collectionVariables.set(varName, json.data.id);
    }
}

function authCookies() {
    return {
        access: pm.collectionVariables.get('access_token'),
        xsrf: pm.collectionVariables.get('xsrf_token')
    };
}
