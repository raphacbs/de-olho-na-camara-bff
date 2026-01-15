package br.com.deolhonacamara.api;

/**
 * Classe que define os códigos de erro de negócio utilizados na aplicação.
 * Estes códigos são usados para identificar e categorizar diferentes tipos
 * de erros que podem ocorrer durante a execução das operações de negócio.
 * 
 * @author Personal Finance Team
 * @version 1.0
 * @since 1.0
 */
public class BusinessCode {
   /** Código para token não encontrado ou expirado */
   public final static String TOKEN_NOT_FOUND_OR_EXPIRED = "AUTH-001";
   /** Código para email inválido */
   public final static String INVALID_EMAIL = "AUTH-002";
   /** Código para email não pode estar vazio */
   public final static String EMAIL_NOT_EMPTY = "AUTH-003";
   /** Código para senha não pode estar vazia */
   public final static String PASSWORD_NOT_EMPTY = "AUTH-004";
   /** Código para senha inválida */
   public final static String INVALID_PASSWORD = "AUTH-005";
   /** Código para usuário inativo */
   public final static String INACTIVE_USER = "AUTH-006";

   public final static String USER_ALREADY_EXISTS = "AUTH-007";

   /** Código para usuário inválido */
   public final static String INVALID_USER = "AUTH-007";
   public final static String REFRESH_TOKEN_EXPIRED = "AUTH-008";
   public final static String TOKEN_NOT_FOUND = "AUTH-009";
   public final static String TOKEN_EXPIRED = "AUTH-010";
   public final static String TOKEN_INVALID = "AUTH-011";

   /** Código para carteira não encontrada */
   public final static String WALLET_NOT_FOUND = "WALLET-001";
   /** Código para conta não encontrada */
   public final static String ACCOUNT_NOT_FOUND = "ACCOUNT-001";

   /** Código para transação não encontrada */
   public final static String TRANSACTION_NOT_FOUND = "TRANSACTION-001";
   /** Código para transação recorrente não encontrada */
   public final static String RECURRING_TRANSACTION_NOT_FOUND = "RECURRING-001";
   /** Código para cartão de crédito não encontrado */
   public final static String CREDIT_CARD_NOT_FOUND = "CREDIT-CARD-001";
   /** Código para usuário não é proprietário do cartão de crédito */
   public final static String USER_NOT_OWNER_CREDIT_CARD = "CREDIT-CARD-002";
   /** Código para orçamento mensal não encontrado */
   public final static String MONTHLY_BUDGET_NOT_FOUND = "BUDGET-001";

   /** Código para político não encontrado */
   public final static String POLITICIAN_NOT_FOUND = "POLITICIAN-001";
}
