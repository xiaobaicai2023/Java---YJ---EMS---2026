<template>
  <div class="box">
    <ul>
      <template v-for="item in data" :key="item.name">
        <li @click="toggleSubMenu(item)">
          {{ item.name }}
          <recursive-menu v-if="item.showChildren" :data="item.children" />
        </li>
      </template>
    </ul>
  </div>
</template>

<script>
import {defineComponent} from 'vue';

const RecursiveMenu = defineComponent({
  name: 'RecursiveMenu',
  props: {
    data: {
      type: Array,
      required: true
    }
  },
  template: `
    <ul>
      <li v-for="item in data" :key="item.name" @click="toggleSubMenu(item)">
        {{ item.name }}
        <recursive-menu v-if="item.showChildren" :data="item.children"/>
      </li>
    </ul>
  `,
  methods: {
    toggleSubMenu(item) {
      if (item.children) {
        item.showChildren = !item.showChildren;
      }
    }
  }
});

export default defineComponent({
  name: 'App',
  components: {
    RecursiveMenu
  },
  data() {
    return {
      data: [
        {
          name: 'a',
          children: [
            {
              name: 'a-1',
              children: [
                {
                  name: 'a-1-1',
                  children: [
                    {
                      name: 'a-1-1-1',
                      children: [
                        {
                          name: 'a-1-1-1-1'
                        }
                      ]
                    }
                  ]
                }
              ]
            },
            {
              name: 'a-2',
            }
          ]
        },
        // 其他顶级菜单项...
      ]
    };
  }
});
</script>