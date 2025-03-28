<template>
  <div>
    <input v-model="termo" placeholder="Buscar operadora..." @change="buscar" />
    <ul>
      <li v-for="op in resultados" :key="op.Registro_ANS">
        <strong>{{ op.Nome_Fantasia }}</strong><br />
        <small>{{ op.Razao_Social }}</small><br />
        <small>{{ op.CNPJ }} - {{ op.Cidade }}/{{ op.UF }}</small>
      </li>
    </ul>

    <p v-if="resultados.length === 0 && termo">Nenhum resultado encontrado.</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      termo: '',
      resultados: []
    };
  },
  methods: {
    async buscar() {
      try {
        const response = await fetch(`http://localhost:5000/operadoras?busca=${this.termo}`);
        if (!response.ok) throw new Error("Erro na requisição");
        const json = await response.json();
        console.log(json); // veja se o conteúdo aparece
        this.resultados = json;
      } catch (error) {
        console.error("Erro:", error);
      }
    }


  }
};
</script>
