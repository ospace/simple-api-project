/**
 * 개발자용 공통 라이브러리 
 */

this.cmm = (function () {
return {
    roleName: function(role, roles) {
        let found = roles && roles[role];
        return found ? found.name : '-';
    },
    enableName: function(enable) {
        return enable?'사용':'미사용';
    },
    genderName: function(gender, genders) {
        return genders ? genders[gender] : '-';
    },
    requiredName: function(required) {
        return required ? '필수' : '선택';
    }
};
})();
