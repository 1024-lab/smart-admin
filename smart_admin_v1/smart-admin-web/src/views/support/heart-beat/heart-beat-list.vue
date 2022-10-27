<template>
  <Card class="warp-card" dis-hover>
    <Form class="tools" inline>
      <FormItem>
        <Button
          @click="queryHeartBeatRecord"
          icon="md-refresh"
          type="primary"
          v-privilege="'heart-beat-query'"
        >刷新</Button>
      </FormItem>
    </Form>
    <Alert>
      <h3>Smart-Heart-Beat 心跳服务介绍：</h3>
      <pre>
简介：Smart-Heart-Beat 是心跳服务，用于监测Java应用的状态等其他信息。

原理：
- Java后端会在项目启动的时候开启一个线程，每隔一段时间将该应用的IP、进程号更新到数据库t_heart_beat_record表中。
用途：
· 在各个环境（无论开发、测试、生产）能统一看到所有启动的服务列表。
· 检测Java应用是否存活。
· ※强烈推荐※ 当某些业务只允许有一个服务启动的时候，用于排查是否别人也启动的服务！！
      </pre>
    </Alert>
    <Table :columns="columns" :data="tableData" :loading="tableLoading" border></Table>
    <Page
      :current="searchFrom.pageNum"
      :page-size="searchFrom.pageSize"
      :show-sizer="true"
      :show-total="true"
      :total="pageTotal"
      @on-change="changePage"
      @on-page-size-change="changePageSize"
      style="margin:24px 0;text-align:right;"
    ></Page>
  </Card>
</template>
<script>
import { heartBeatApi } from '@/api/heart-beat';

export default {
  name: 'HeartBeatList',
  components: {},
  props: {},
  data() {
    return {
      searchFrom: {
        pageSize: 10,
        pageNum: 1
      },
      tableLoading: false,
      pageTotal: 1,
      tableData: [],
      columns: [
        {
          title: 'Id',
          key: 'id',
          width: 100
        },
        {
          title: '路径',
          key: 'projectPath'
        },
        {
          title: '进程号',
          key: 'processNo'
        },
        {
          title: '服务器ip',
          key: 'serverIp'
        },
        {
          title: '进程启动时间',
          key: 'processStartTime'
        },
        {
          title: '心跳时间 ',
          key: 'heartBeatTime'
        }
      ]
    };
  },
  mounted() {
    this.queryHeartBeatRecord();
  },
  methods: {
    // 查询心跳记录
    async queryHeartBeatRecord() {
      try {
        this.tableLoading = true;
        let result = await heartBeatApi.queryHeartBeatRecord(this.searchFrom);
        this.tableData = result.data.list;
        this.pageTotal = result.data.total;
        this.tableLoading = false;
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.tableLoading = false;
      }
    },
    // 页码改变
    changePage(pageNum) {
      this.searchFrom.pageNum = pageNum;
      this.queryHeartBeatRecord();
    },
    // 改变每页显示数据条数
    changePageSize(pageSize) {
      this.searchFrom.pageNum = 1;
      this.searchFrom.pageSize = pageSize;
      this.queryHeartBeatRecord();
    }
  }
};
</script>
