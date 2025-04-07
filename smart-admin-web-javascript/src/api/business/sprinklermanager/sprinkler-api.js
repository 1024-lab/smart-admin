/*
 * 喷头信息
 *
 * @Author:    芦苇
 */
import {postRequest, getRequest, postDownload} from '/@/lib/axios';

export const sprinklerApi = {
    // 新建喷头 @author 开云
    create: (param) => {
        return postRequest('/oa/enterprise/create', param);
    },

    // 删除企业 @author 开云
    delete: (sprinklerId) => {
        return getRequest(`/oa/enterprise/delete/${sprinklerId}`);
    },

    // 查询喷头详情 @author 芦苇
    detail: (sprinklerId) => {
        return getRequest(`/sprinklermanager/sprinkler/get/${sprinklerId}`);
    },

    // 分页查询企业模块 @author 开云
    pageQuery: (param) => {
        return postRequest('sprinklermanager/sprinkler/page/query', param);
    },

    // 导出企业数据excel @author 卓大
    exportExcel: (param) => {
        return postDownload('/oa/enterprise/exportExcel', param);
    },

    //企业列表查询 含数据范围 @author 开云
    queryList: (type) => {
        let query = '';
        if (type) {
            query = `?type=${type}`;
        }
        return getRequest(`/oa/enterprise/query/list${query}`);
    },

    // 编辑企业 @author 开云
    update: (param) => {
        return postRequest('/sprinklermanager/sprinkler/update', param);
    },
    // 企业全部员工List @author yandy
    employeeList: (param) => {
        return postRequest('/oa/enterprise/employee/list', param);
    },
    // 分页查询企业员工List @author 卓大
    queryPageEmployeeList: (param) => {
        return postRequest('/oa/enterprise/employee/queryPage', param);
    },
    // 添加员工 @author yandy
    addEmployee: (param) => {
        return postRequest('/oa/enterprise/employee/add', param);
    },

    // 删除员工 @author yandy
    deleteEmployee: (param) => {
        return postRequest('/oa/enterprise/employee/delete', param);
    },

};
