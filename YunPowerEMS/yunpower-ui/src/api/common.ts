import { PaginationProps } from "@arco-design/web-vue";
import { reactive } from "vue";

export const BasePagination = () => {
  return {
    current: 1,
    pageSize: 20,
    showPageSize: true,
    showJumper: true,
    size: "medium",
    defaultPageSize: 20,
    showTotal: true,
    pageSizeOptions: [10, 20, 30, 50, 100],
  };
};

export const Pagination: PaginationProps = reactive({
  ...BasePagination(),
  showPageSize: true,
  showJumper: true,
  size: "medium",
  defaultPageSize: 20,
  showTotal: true,
  pageSizeOptions: [10, 20, 30, 50, 100],
});
