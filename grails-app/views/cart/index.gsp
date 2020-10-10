<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="layout" content="main">
        <title>Items for Sale</title>
    </head>
    <body>
        <h1 style="text-align: center;">Our Shop</h1>
        <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
        </g:if>
        <div id="feedback" style="text-align: center;"></div>
        <div id="shop">
          <table  style="background-color: #f7f7f7; width: 700px; margin: 1.5em auto 0 auto; border-left: 1px solid #dedede; border-right: 1px solid #dedede;">
            <thead>
              <tr>
                <th style="width:40%">Item Name</th>
                <th style="width: 15%">Price</th>
                <th style="width: 15%">Qty</th>
                <th style="width: 30%">Add to Cart</th>
              </tr>
            </thead>
            <tbody></tbody>
          </table>
        <g:each in="${items}" status="i" var="item">
          <g:form name="itemsForm${i}" action="addToCart"  method="POST">
            <table style="width: 700px; margin: 0 auto 0 auto; border-left: 1px solid #dedede; border-right: 1px solid #dedede; "class="${(i % 2) == 0 ? 'odd' : 'even'}">
              <tbody>
                <tr>
                  <td  style="width: 40%">${fieldValue(bean:item, field:'name')} <input type="hidden" name="id" value="${fieldValue(bean:item, field:'id')}" /></td>
                  <td style="width: 15%">&#36;${fieldValue(bean:item, field:'price')} </td>
                  <td style="width: 15%"><input type="text" name="quantity" size="3" value="1" /></td>
                  <td style="width: 30%">
                      <g:submitButton name='add' value='Add to cart' class="btn btn-primary" onclick="addToCart('itemsForm${i}', 'feedbackDiv')"/>
                  </td>
                </tr>
              </tbody>
            </table>
           </g:form>
        </g:each>
        <div id="feedbackDiv" style="text-align: center;"></div>
        <p style="text-align: center;">
        	<g:link action="showCart">See your cart</g:link>
        </p>
        </div>
        <content tag="javascript">
            <asset:javascript src="custom/cart.js"/>
        </content>
    </body>
</html>
