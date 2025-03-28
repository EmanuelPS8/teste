<script setup lang="ts">
import { ref } from 'vue'

// Definindo props corretamente
const props = defineProps<{
  nome: string
}>()

const comentario = ref('')

// função normal fora de classe
function comentar() {
  comentario.value = 'Um mega comentário!'
}

// função que acessa props direto
function detalhes() {
  return `Nome: ${props.nome}`
}
</script>

<template>
  <div>
    <button @click="comentar">Comentar</button>
    <p>{{ comentario }}</p>
    <p>{{ detalhes() }}</p>
  </div>
</template>
