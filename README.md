# AppRestaurante
Projeto OndeComer
Descrição do Projeto
O projeto OndeComer é um aplicativo Android desenvolvido em Java com o objetivo de ajudar usuários a organizarem restaurantes, lanchonetes e locais de alimentação em diferentes categorias.
O aplicativo permite:
•	Cadastrar restaurantes
•	Editar informações
•	Excluir registros
•	Visualizar restaurantes em listas
•	Separar restaurantes por categorias
•	Associar os dados ao usuário autenticado
•	Salvar dados localmente utilizando SQLite
O desenvolvimento foi realizado utilizando Android Studio, RecyclerView, SQLite e Firebase Authentication.

Objetivo do Projeto
O objetivo principal do projeto foi praticar conceitos fundamentais do desenvolvimento Android nativo, incluindo:
•	CRUD completo
•	Persistência de dados com SQLite
•	RecyclerView
•	Adapters
•	Navegação entre telas
•	Interface dinâmica
•	Integração com Firebase Authentication
•	Manipulação de eventos e callbacks
•	BottomSheetDialog
Além da parte técnica, o projeto também teve como objetivo desenvolver raciocínio lógico, organização de código e prática de arquitetura básica de aplicativos Android.

Tecnologias Utilizadas
Linguagem
•	Java
IDE
•	Android Studio
Banco de Dados
•	SQLite
Autenticação
•	Firebase Authentication
Componentes Android
•	RecyclerView
•	CardView
•	BottomSheetDialog
•	MaterialToolbar
•	FloatingActionButton
•	AlertDialog
•	ItemTouchHelper

Estrutura do Aplicativo
O aplicativo possui as seguintes telas:
Tela de Login
Responsável pela autenticação do usuário utilizando Firebase Authentication.
Funcionalidades
•	Login com email e senha
•	Validação de campos
•	Redirecionamento para a tela principal
<img width="886" height="488" alt="image" src="https://github.com/user-attachments/assets/f32c5d47-878b-4b7a-9efa-0f42b7843eff" />
 
Tela Principal
Tela inicial do aplicativo.
O usuário pode selecionar diferentes categorias:
•	Quero ir
•	Já fui
•	Delivery
Cada categoria abre uma lista diferente de restaurantes.
<img width="886" height="496" alt="image" src="https://github.com/user-attachments/assets/69d7820b-b8c0-45a3-8930-42ed8582f855" />

Funcionalidades
•	Navegação entre categorias
•	Toolbars dinâmicas
•	Interface personalizada para cada categoria
 <img width="886" height="508" alt="image" src="https://github.com/user-attachments/assets/1dbf4504-bf66-4bec-923c-312d9185a59d" />
<img width="886" height="514" alt="image" src="https://github.com/user-attachments/assets/0e94bf17-72be-4b13-af40-befbad6d17f5" />
<img width="886" height="492" alt="image" src="https://github.com/user-attachments/assets/7437a4e7-481f-405e-b9be-7cfc81066aea" />

 
Tela de Listagem
Tela responsável por exibir os restaurantes cadastrados utilizando RecyclerView.
Funcionalidades
<img width="845" height="1278" alt="image" src="https://github.com/user-attachments/assets/b3c24827-2044-4cf1-af26-20de87297b49" />

•	Exibição dos restaurantes
•	RecyclerView dinâmica
•	Cards personalizados
•	RatingBar
•	Swipe para deletar
•	Clique para edição
 
Banco de Dados SQLite
O projeto utiliza SQLite para armazenamento local.
Estrutura da tabela
<img width="886" height="352" alt="image" src="https://github.com/user-attachments/assets/5f77e804-8b84-474e-89d3-420574b42beb" />

 Explicação dos campos
Campo	Função
id	Identificador único do restaurante
nome	Nome do restaurante
resumo	Descrição ou comentário
categoria	Categoria escolhida pelo usuário
nota	Nota do restaurante
usuarioId	Identificação do usuário autenticado

Funcionamento do CRUD
CREATE — Cadastro
O usuário pode cadastrar restaurantes utilizando um BottomSheetDialog.
Os dados são salvos no SQLite.
Campos cadastrados
•	Nome
•	Resumo
•	Nota
•	Categoria
•	Usuário
READ — Leitura dos Dados
Os dados são recuperados do SQLite utilizando SELECT.
Após a recuperação:
1.	Os dados são transformados em objetos da classe Restaurante
2.	Os objetos são adicionados em um ArrayList
3.	O Adapter envia os dados para a RecyclerView
Fluxo
SQLite
↓
Objeto Restaurante
↓
ArrayList
↓
Adapter
↓
RecyclerView


UPDATE — Atualização
O usuário pode editar um restaurante clicando sobre o card da RecyclerView.
O aplicativo abre novamente o BottomSheetDialog preenchido com os dados atuais.
Após a alteração, o SQLite executa um UPDATE.
<img width="665" height="666" alt="image" src="https://github.com/user-attachments/assets/eb38672a-7304-48d4-a532-8fcba04babd2" />

 
DELETE — Exclusão
O usuário pode remover restaurantes utilizando swipe lateral.
Ao arrastar o item:
1.	O aplicativo exibe um AlertDialog de confirmação
2.	Caso o usuário confirme, o restaurante é removido do SQLite
3.	A RecyclerView é atualizada automaticamente
4.	<img width="521" height="1108" alt="image" src="https://github.com/user-attachments/assets/57f8691a-bfe2-4633-b966-dfc73326a3c5" />

 
RecyclerView e Adapter
A RecyclerView foi utilizada para exibir listas de restaurantes de forma dinâmica.
Responsabilidades do Adapter
•	Exibir dados dos restaurantes
•	Fazer ligação entre ArrayList e RecyclerView
•	Configurar eventos de clique
•	Atualizar itens
•	Remover itens
ViewHolder
O ViewHolder foi utilizado para melhorar desempenho e reutilização de componentes visuais.

Swipe e ItemTouchHelper
O projeto utiliza ItemTouchHelper.SimpleCallback para implementar swipe nos cards da RecyclerView.
Funcionalidades
•	Detectar gesto de arrastar
•	Exibir confirmação de exclusão
•	Atualizar RecyclerView

Toolbars Dinâmicas
Cada categoria possui uma toolbar personalizada.
Alterações dinâmicas
•	Título
•	Subtítulo
•	Gradiente
•	Cores
Essas alterações acontecem de acordo com a categoria selecionada pelo usuário.

BottomSheetDialog Dinâmico
O formulário de cadastro e edição também é dinâmico.
Dependendo da categoria:
•	As cores mudam
•	Os textos mudam
•	O botão muda de estilo

Principais Dificuldades Encontradas
Durante o desenvolvimento do projeto foram encontrados diversos desafios técnicos.
Exemplos
•	NullPointerException
•	Manipulação de RecyclerView
•	Integração entre SQLite e RecyclerView
•	Configuração de swipe
•	Atualização automática da lista
•	Problemas de contexto e IDs
•	Toolbars dinâmicas
•	Callbacks e interfaces
Esses problemas ajudaram no aprendizado prático do desenvolvimento Android.

Aprendizados Obtidos
O projeto permitiu desenvolver conhecimentos importantes sobre:
•	Estrutura de aplicativos Android
•	CRUD completo
•	Banco de dados local
•	Programação orientada a objetos
•	Adapters e RecyclerView
•	Eventos e callbacks
•	Organização de código
•	Interface dinâmica
•	Debug de aplicações Android
Além da parte técnica, o projeto também contribuiu para o desenvolvimento da autonomia no aprendizado e resolução de problemas.

Considerações Finais
O projeto OndeComer permitiu aplicar conceitos fundamentais do desenvolvimento Android em um cenário prático e funcional.
Durante o desenvolvimento foi possível compreender como diferentes componentes do Android trabalham juntos, desde persistência de dados até interface gráfica e interação do usuário.
O aplicativo continua em evolução e pode receber melhorias futuras, como:
•	Integração com API
•	Upload de imagens
•	Sincronização em nuvem
•	Melhorias visuais
•	Filtros e pesquisa
•	Compartilhamento de restaurantes

Conclusão
O desenvolvimento do projeto foi importante para consolidar conhecimentos de programação Android, banco de dados e estruturação de aplicações móveis.
O aplicativo conseguiu atingir os objetivos propostos e serviu como prática real de desenvolvimento mobile utilizando Java e Android Studio.
