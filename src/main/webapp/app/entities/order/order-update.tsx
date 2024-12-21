import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { getEntities as getShippings } from 'app/entities/shipping/shipping.reducer';
import { getEntities as getBillings } from 'app/entities/billing/billing.reducer';
import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { createEntity, getEntity, reset, updateEntity } from './order.reducer';

export const OrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customers = useAppSelector(state => state.customer.entities);
  const shippings = useAppSelector(state => state.shipping.entities);
  const billings = useAppSelector(state => state.billing.entities);
  const payments = useAppSelector(state => state.payment.entities);
  const orderEntity = useAppSelector(state => state.order.entity);
  const loading = useAppSelector(state => state.order.loading);
  const updating = useAppSelector(state => state.order.updating);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);
  const orderStatusValues = Object.keys(OrderStatus);

  const handleClose = () => {
    navigate(`/order${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCustomers({}));
    dispatch(getShippings({}));
    dispatch(getBillings({}));
    dispatch(getPayments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.totalPrice !== undefined && typeof values.totalPrice !== 'number') {
      values.totalPrice = Number(values.totalPrice);
    }
    values.orderDate = convertDateTimeToServer(values.orderDate);
    values.expirationDate = convertDateTimeToServer(values.expirationDate);

    const entity = {
      ...orderEntity,
      ...values,
      customer: customers.find(it => it.id.toString() === values.customer?.toString()),
      shipping: shippings.find(it => it.id.toString() === values.shipping?.toString()),
      billing: billings.find(it => it.id.toString() === values.billing?.toString()),
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          orderDate: displayDefaultDateTime(),
          expirationDate: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...orderEntity,
          orderDate: convertDateTimeFromServer(orderEntity.orderDate),
          expirationDate: convertDateTimeFromServer(orderEntity.expirationDate),
          customer: orderEntity?.customer?.id,
          shipping: orderEntity?.shipping?.id,
          billing: orderEntity?.billing?.id,
          payment: orderEntity?.payment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissAsstApp.order.home.createOrEditLabel" data-cy="OrderCreateUpdateHeading">
            <Translate contentKey="larissAsstApp.order.home.createOrEditLabel">Create or edit a Order</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissAsstApp.order.status')}
                id="order-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {orderStatusValues.map(orderStatus => (
                  <option value={orderStatus} key={orderStatus}>
                    {translate(`larissAsstApp.OrderStatus.${orderStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('larissAsstApp.order.totalPrice')}
                id="order-totalPrice"
                name="totalPrice"
                data-cy="totalPrice"
                type="text"
              />
              <ValidatedField
                label={translate('larissAsstApp.order.trackId')}
                id="order-trackId"
                name="trackId"
                data-cy="trackId"
                type="text"
              />
              <ValidatedField
                label={translate('larissAsstApp.order.orderDate')}
                id="order-orderDate"
                name="orderDate"
                data-cy="orderDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('larissAsstApp.order.expirationDate')}
                id="order-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="order-customer"
                name="customer"
                data-cy="customer"
                label={translate('larissAsstApp.order.customer')}
                type="select"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-shipping"
                name="shipping"
                data-cy="shipping"
                label={translate('larissAsstApp.order.shipping')}
                type="select"
              >
                <option value="" key="0" />
                {shippings
                  ? shippings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-billing"
                name="billing"
                data-cy="billing"
                label={translate('larissAsstApp.order.billing')}
                type="select"
              >
                <option value="" key="0" />
                {billings
                  ? billings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-payment"
                name="payment"
                data-cy="payment"
                label={translate('larissAsstApp.order.payment')}
                type="select"
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrderUpdate;
