<!--
  * 首页 用户头部信息
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-12 22:34:00
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
  *
-->
<template>
  <div class="user-header">
    <a-page-header :title="welcomeSentence" :sub-title="departmentName" >
      <template #tags>
        <a-tag color="blue">努力工作</a-tag>
        <a-tag color="success">主动 / 皮实 / 可靠 </a-tag>
        <a-tag color="error">自省 / 精进 / 创新</a-tag>
      </template>
      <template #extra>
        <p>{{ dayInfo }}</p>
      </template>
      <a-row class="content">
        <span class="heart-sentence">
          <h3>{{ heartSentence }}</h3>
          <p class="last-login-info">{{ lastLoginInfo }}</p>
          <div></div>
        </span>
        <div class="weather">
          <iframe
            width="100%"
            scrolling="no"
            height="60"
            frameborder="0"
            allowtransparency="true"
            src="//i.tianqi.com/index.php?c=code&id=12&icon=1&num=5&site=12"
          ></iframe>
        </div>
      </a-row>
    </a-page-header>
  </div>
</template>
<script setup lang="ts">
  import { computed } from 'vue';
  import { useUserStore } from '/@/store/modules/system/user';
  import uaparser from 'ua-parser-js';
  import { Solar, Lunar } from 'lunar-javascript';
  import _ from 'lodash';

  const userStore = useUserStore();

  const departmentName = computed(() => useUserStore.departmentName);

  // 欢迎语
  const welcomeSentence = computed(() => {
    let sentence = '';
    let now = new Date().getHours();
    if (now > 0 && now <= 6) {
      sentence = '午夜好，';
    } else if (now > 6 && now <= 11) {
      sentence = '早上好，';
    } else if (now > 11 && now <= 14) {
      sentence = '中午好，';
    } else if (now > 14 && now <= 18) {
      sentence = '下午好，';
    } else {
      sentence = '晚上好，';
    }
    return sentence + userStore.$state.actualName;
  });

  //上次登录信息
  const lastLoginInfo = computed(() => {
    let info = '';
    if (userStore.$state.lastLoginTime) {
      info = info + '上次登录:' + userStore.$state.lastLoginTime;
    }
    if (userStore.$state.lastLoginIp) {
      info = info + '; IP:' + userStore.$state.lastLoginIp;
    }
    if (userStore.$state.lastLoginUserAgent) {
      let ua = uaparser(userStore.$state.lastLoginUserAgent);
      info = info + '; 设备:';
      if (ua.browser.name) {
        info = info + ' ' + ua.browser.name;
      }
      if (ua.os.name) {
        info = info + ' ' + ua.os.name;
      }
      let device = ua.device.vendor ? ua.device.vendor + ua.device.model : null;
      if (device) {
        info = info + ' ' + device;
      }
    }
    return info;
  });

  //日期、节日、节气
  const dayInfo = computed(() => {
    //阳历
    let solar = Solar.fromDate(new Date());
    let day = solar.toString();
    let week = solar.getWeekInChinese();
    //阴历
    let lunar = Lunar.fromDate(new Date());
    let lunarMonth = lunar.getMonthInChinese();
    let lunarDay = lunar.getDayInChinese();
    //节气
    let jieqi = lunar.getPrevJieQi().getName();
    let next = lunar.getNextJieQi();
    let nextJieqi = next.getName() + ' ' + next.getSolar().toYmd();

    return `${day} 星期${week}，农历${lunarMonth}${lunarDay}（当前${jieqi}，${nextJieqi} ）`;
  });

  // 毒鸡汤
  const heartSentenceArray = [
    '每个人的一生好比一根蜡烛，看似不经意间散发的光和热，都可能照亮和温暖他人。这是生活赋予我们的智慧，也让我们在寻常的日子成为一个温暖善良的人。',
    '立规矩的目的，不是禁锢、限制，而是教育；孩子犯了错，父母不能帮孩子逃避，而应该让孩子学会承担责任。让孩子有面对错误的诚实和勇气，这才是立规矩的意义所在。',
    '人这一辈子，格局大了、善良有了，成功自然也就近了。格局越大，人生越宽。你的人生会是什么样，与你在为人处世时的表现有很大关系。世间美好都是环环相扣的，善良的人总不会被亏待。',
    '平日里的千锤百炼，才能托举出光彩时刻；逆境中的亮剑、失败后的奋起，才能让梦想成真。哪有什么一战成名，其实都是百炼成钢。“天才”都是汗水浇灌出来的，天赋或许可以决定起点，但唯有坚持和努力才能达到终点。',
    '家，不在于奢华，而在于温馨；家，不在于大小，而在于珍惜。在家里，有父母的呵护，有爱人的陪伴，有子女的欢笑。一家人整整齐齐、和和睦睦，就是人生最大的幸福！',
    '每一个不向命运低头、努力生活的人，都值得被尊重。',
    '青年的肩上，从不只有清风明月，更有责任担当。岁月因青春慨然以赴而更加美好，世间因少年挺身向前而更加瑰丽。请相信，不会有人永远年轻，但永远有人年轻。',
    '人生路上，总有人走得比你快，但不必介意，也不必着急。一味羡慕别人的成绩，只会给自己平添压力、徒增烦恼。不盲从别人的脚步，坚定目标，才能找到自己的节奏，进而逢山开路、遇水搭桥。',
    '如果你真的在乎一个人，首先要学会的就是感恩对方的好。这样，对方才会在和你的相处中找到价值感，相处起来也会更加舒适愉悦。',
    '一个人只有心里装得下别人，有换位思考的品质，有为他人谋幸福的信念，才能真正做到慷慨施予。同样，也只有赠人玫瑰而无所求时，你才会手有余香、真有所得。',
  ];
  const heartSentence = computed(() => {
    return heartSentenceArray[_.random(0, heartSentenceArray.length - 1)];
  });
</script>
<style scoped lang="less">
  .user-header {
    width: 100%;
    background-color: #fff;
    margin-bottom: 10px;

    .heart-sentence {
      width: calc(100% -660px);
      h3 {
        color: rgba(0, 0, 0, 0.75);
      }
    }

    .content {
      display: flex;
      justify-content: space-between;
      .weather {
        width: 650px;
      }
    }

    .last-login-info {
      font-size: 13px;
      color: rgba(0, 0, 0, 0.45);
      overflow-wrap: break-word;
    }
  }
</style>
