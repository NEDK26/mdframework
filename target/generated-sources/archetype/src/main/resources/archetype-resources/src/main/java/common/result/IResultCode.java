#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.result;

public interface IResultCode {
    Integer getCode();
    String getMessage();
}
