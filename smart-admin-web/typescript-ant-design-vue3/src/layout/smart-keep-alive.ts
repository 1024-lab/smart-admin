/*
 *  keep-alive
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:39:54
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';

export function smartKeepAlive() {
  const route = useRoute();
  const router = useRouter();
  // 需要keep-alive的页面
  const keepAliveIncludes = computed(() => {
    return useUserStore().keepAliveIncludes || [];
  });
  
  // ----------------------- iframe相关 -----------------------

  // 当前路由是否为不需要缓存的iframe页面
  const iframeNotKeepAlivePageFlag = computed(() => route.meta.frameFlag && !route.meta.keepAlive);
  // 打开中的tagNav
  const tagNav = computed(() => useUserStore().getTagNav || []);
  // 已打开的iframe列表
  const keepAliveIframePages = computed(() => {
    let routes = router.getRoutes();
    return routes.filter((e) => e.meta.frameFlag && e.meta.keepAlive && tagNav.value.some((t) => t.menuName == e.name));
  });
  return {
    route,
    keepAliveIncludes,
    iframeNotKeepAlivePageFlag,
    keepAliveIframePages,
  };
}
