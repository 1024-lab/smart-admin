/**
* @description：department-employee-selector
* @author: zhuoda
* @date: 2020/10/30 13:56
*/

<template>

  <div style="padding: 20px">
    <template v-if="isRadio">
      <van-radio-group v-model="radioSelectEmployeeId">
        <van-radio
          v-for="item in employeeList"
          :key="item.id"
          :name="item.id"
          style="margin-top: 5px"
          @click="clickRadio"
        >
          {{
            item.actualName
          }}
        </van-radio>
      </van-radio-group>
    </template>

    <template v-if="!isRadio">
      <van-checkbox-group ref="checkboxGroup" :max="max" v-model="selectEmployeeList">
        <van-checkbox
          :key="item.id"
          :name="item.id"
          v-model="item._checked"
          v-for="item in employeeList"
          style="margin-top: 5px">
          {{
            item.actualName
          }}
        </van-checkbox>
      </van-checkbox-group>
    </template>
    <br>
    <van-button v-if="!isRadio" type="primary" size="small" @click="checkAll" style="margin:0 10px">全选</van-button>
    <van-button v-if="!isRadio" type="info" size="small" @click="toggleAll">反选</van-button>
  </div>

</template>

<script>
import { systemConfigApi } from '@/api/system-config';
import { employeeApi } from 'api/employee';

export default {
  name: 'DepartmentEmployeeSelector',
  props: {
    // 最大数
    max: Number,
    isRadio: Boolean
  },

  data() {
    return {
      selectEmployeeList: [],
      employeeList: [],
      radioSelectEmployeeId: null,
      radioSelectEmployeeName: null
    };
  },
  created() {
    this.loadEmployeeList();
  },
  methods: {
    updateRadioSelectEmployeeId(employeeId) {
      this.radioSelectEmployeeId = employeeId;
    },
    clickRadio(e, val) {
      this.radioSelectEmployeeName = e.target.innerText;
    },
    getSelected() {
      if (this.isRadio) {
        return [{
          id: this.radioSelectEmployeeId,
          actualName: this.radioSelectEmployeeName
        }];
      } else {
        const result = [];
        for (const employee of this.employeeList) {
          if (this.selectEmployeeList.indexOf(employee.id) > -1) {
            result.push(Object.assign({}, employee));
          }
        }
        return result;
      }
    },
    async loadEmployeeList() {
      this.$smart.loading();
      try {
        const departmentIdResult = await systemConfigApi.getConfigListByKey('crm_school_share_department_id');
        const employeeListResult = await employeeApi.getListEmployeeByDeptId(departmentIdResult.data.configValue);
        this.employeeList = employeeListResult.data.filter(e => e.isDelete === 0 && e.isDisabled === 0).map(e => {
          e._checked = false;
          return e;
        });
      } catch (e) {
        this.$smartSentry.captureException(e);
      } finally {
        this.$smart.loadingClear();
      }
    },
    checkAll() {
      this.$refs.checkboxGroup.toggleAll(true);
    },
    toggleAll() {
      this.$refs.checkboxGroup.toggleAll();
    }
  }

};
</script>

<style scoped>

</style>
