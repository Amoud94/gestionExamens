<template>
    <div>
        <button class="circular ui icon tiny button blue-btn-circle" :disabled="current_page === 1" @click="changePage(1)">
            <i class="angle double left icon"></i>
        </button>
        <button :disabled="!hasPrev"  class="circular ui icon tiny button blue-btn-circle"  @click="changePage(current_page - 1)">
            <i class="angle left icon"></i>
        </button>
        <button v-for="page_number in pages" :disabled="current_page === page_number" @click="changePage(page_number)" class="circular ui icon tiny button blue-btn-circle">
            {{page_number}}
        </button>
        <button :disabled="!hasNext" class="circular ui icon tiny button blue-btn-circle" @click="changePage(current_page + 1)">
            <i class="angle right icon"></i>
        </button>
        <button class="circular ui icon tiny button blue-btn-circle" :disabled="current_page === totalPages" @click="changePage(totalPages)">
            <i class="angle double right icon"></i>
        </button>
    </div>
</template>

<script>
    export default {
        name:'pagination',
        props: {
            current_page: {
                type: Number,
                default: 1
            },
            count: {
                type: Number,
                default: 0
            },
            limit: {
                type: Number,
                default: 5
            },
            pageRange: {
                type: Number,
                default: 10
            }
        },
        methods:{
            changePage(page) {
                this.$emit("setPage", page);
            }
        },
        computed:{
            pages() {
                let pages = [];
                for (let i = this.rangeStart; i <= this.rangeEnd; i++) {
                    pages.push(i);
                }
                return pages;
            },
            rangeStart() {
                let start = this.current_page - this.pageRange;
                return start > 0 ? start : 1;
            },
            rangeEnd() {
                let end = (this.current_page + this.pageRange) + 1 ;
                return end < this.totalPages ? end : this.totalPages;
            },
            totalPages() {
                return Math.ceil(this.count / this.limit);
            },
            hasPrev() {
                return this.current_page > 1;
            },
            hasNext() {
                return this.current_page < this.totalPages;
            }
        }
    }
</script>
