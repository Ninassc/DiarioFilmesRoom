# 🎬 Diário de Filmes (Room + Studio Ghibli API)

O **Diário de Filmes** é um aplicativo Android desenvolvido em Kotlin utilizando **Jetpack Compose** e **Architecture Components**. O projeto evoluiu de uma persistência SQLite manual para uma arquitetura moderna usando **Room Database** e padrão **Single Source of Truth (SSOT)** com a **Studio Ghibli API**.

---

## 🚀 Arquitetura e Padrão Single Source of Truth (SSOT)

O projeto segue estritamente o princípio **Offline-First / Single Source of Truth**:

* **A UI (Tela) nunca acessa a API diretamente.** Ela apenas observa a fonte de dados local (banco Room) através de `Flow`.
* **Fluxo de Sincronização:** Quando uma busca é executada, o `Repository` realiza a requisição na **Studio Ghibli API**, recebe a resposta e a grava no banco local **Room**. O Room, por sua vez, notifica a UI via `Flow` com a lista atualizada.
* **Resiliência Offline:** Erros de rede durante a busca alteram apenas o estado de sincronização (`BuscaState.Error`), exibindo uma mensagem de aviso para o usuário sem limpar ou perder a lista de filmes já gravada no banco.

```
                  ┌──────────────────────┐
                  │  Studio Ghibli API   │
                  └──────────┬───────────┘
                             │ (suspend fetch)
                             ▼
 ┌──────────┐     ┌──────────────────────┐     ┌──────────────────────┐
 │    UI    │ ◄───┤      Repository      ├────►│    Room Database     │
 └──────────┘     └──────────────────────┘     └──────────────────────┘
   (Observa           (Grava na API no          (Única fonte da verdade
    Flow)                  Room)                       para a UI)
```

---

## 🛠️ Principais Atualizações (Migração & Recursos)

### 1. Migração do SQLite Manual para Room
* **`@Entity`:** Definição da entidade de dados `FilmeEntity` mapeando as colunas da tabela local.
* **`@Dao`:** Interface de acesso aos dados expondo consultas reativas via `Flow<List<FilmeEntity>>` e métodos de inserção/atualização.
* **`@Database`:** Configuração da instância de banco de dados nativa do Room.

### 2. Estado de Sincronização Isolado
Para manter a UI limpa e reativa, o estado da requisição e a lista de dados persistidos são gerenciados de forma separada:
* **Lista de Filmes:** Exposta como `StateFlow<List<Filme>>` alimentada continuamente pelo banco local.
* **`BuscaState` (Sealed Interface):** Gerencia exclusivamente o status da sincronização (`Idle`, `Loading`, `Error`), permitindo dar feedback ao usuário sem interferir na exibição dos dados persistidos.

### 3. API Integrada
* **API Escolhida:** [Studio Ghibli API](https://ghibliapi.vercel.app/)
* **Acesso:** API pública e gratuita (não exige autenticação ou chave de acesso).

---

## 🏗️ Tecnologias Utilizadas

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) com Material Design 3
* **Persistência Local:** [Room Database](https://developer.android.com/training/data-storage/room)
* **Comunicação Assíncrona & Reatividade:** Coroutines & Kotlin `Flow` / `StateFlow`
* **Arquitetura:** `AndroidViewModel`, Repository Pattern, Single Source of Truth (SSOT)
