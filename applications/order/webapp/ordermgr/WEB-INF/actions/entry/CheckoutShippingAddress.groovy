/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.party.contact.*;
import org.ofbiz.product.store.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;

// SCIPIO: NOTE: some patches so doesn't crash without userLogin

cart = org.ofbiz.order.shoppingcart.ShoppingCartEvents.getCartObject(request); // SCIPIO: Must use accessor, not this: session.getAttribute("shoppingCart");
party = userLogin?.getRelatedOne("Party", false);
partyId = party?.partyId;
productStoreId = ProductStoreWorker.getProductStoreId(request);

productStoreId = cart.getProductStoreId();
if (productStoreId) {
    productStore = ProductStoreWorker.getProductStore(productStoreId, delegator);
    payToPartyId = productStore.payToPartyId;
}

shippingContactMechId = request.getParameter("shipping_contact_mech_id");
for (shipGroupIndex = 0; shipGroupIndex < cart.getShipGroupSize(); shipGroupIndex++) {
    supplierPartyId = cart.getSupplierPartyId(shipGroupIndex);
    context[shipGroupIndex + "_supplierPartyId"] = supplierPartyId;
}
agreements = from("Agreement").where("partyIdTo", payToPartyId, "partyIdFrom", partyId).filterByDate().cache(true).queryList();
context.agreements = agreements;

context.shoppingCart = cart;
context.userLogin = userLogin;
context.productStoreId = productStoreId;
context.shippingContactMechList = ContactHelper.getContactMech(party, "SHIPPING_LOCATION", "POSTAL_ADDRESS", false);

// SCIPIO: SPECIAL CASE:
// It is possible that a new address was created during event and committed even though a later error occurred
// So to handle this we need a special check to avoid a resubmission of address and to select the newly-created
context.newShipAddrParams = parameters;
if (parameters.shipping_contact_mech_id == "_NEW_") {
    newShipContactMechInfo = parameters.newShipContactMechInfoMap?._NEW_;
    if (newShipContactMechInfo && context.shippingContactMechList) {
        // make sure it appears in contact mech list
        for(cm in context.shippingContactMechList) {
            if (cm.contactMechId == newShipContactMechInfo.contactMechId) {
                parameters.shipping_contact_mech_id = newShipContactMechInfo.contactMechId;
                // Prevent filling the new ship info fields
                context.newShipAddrParams = [:];
                break;
            }
        }
    }
}





