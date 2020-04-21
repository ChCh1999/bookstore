const state = {
    bookOrders: [
        // {
        //     name: 'name1',
        //     price: 32,
        //     count: 2
        // },
        // {
        //     name: 'name2',
        //     price: 41,
        //     count: 3
        // },
        // {
        //     name: 'name3',
        //     price: 21,
        //     count: 5
        // }
    ]
}

const mutations = {
    deleteOrder (state, index) {
        state.bookOrders.splice(index, 1)
    },

    clearCart (state) {
        state.bookOrders = []
    },

    addOrder (state, order) {
        console.log("add order name: " + order.name)
        state.bookOrders.push(order)
    },

    deleteCommitedOrders (state) {
        console.log("deleting commited orders")
        for (var i = 0; i < state.bookOrders.length; i++) {
            if (state.bookOrders[i].commited == true) {
                state.bookOrders.splice(i, 1)
            }
        }
    }
}

const getters = {
    totalPrice: (state) => {
        let sum = 0
        state.bookOrders.forEach((order) => {
            sum += order.price * order.count
        })
        return sum
    },

    totalOrderCount: (state) => {
        return state.bookOrders.length
    }
}

export default {
    state,
    mutations,
    getters
}
