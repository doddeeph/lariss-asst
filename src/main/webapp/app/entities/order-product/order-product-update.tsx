import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { getEntities as getProductDetails } from 'app/entities/product-details/product-details.reducer';
import { createEntity, getEntity, reset, updateEntity } from './order-product.reducer';

export const OrderProductUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const orders = useAppSelector(state => state.order.entities);
  const productDetails = useAppSelector(state => state.productDetails.entities);
  const orderProductEntity = useAppSelector(state => state.orderProduct.entity);
  const loading = useAppSelector(state => state.orderProduct.loading);
  const updating = useAppSelector(state => state.orderProduct.updating);
  const updateSuccess = useAppSelector(state => state.orderProduct.updateSuccess);

  const handleClose = () => {
    navigate(`/order-product${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOrders({}));
    dispatch(getProductDetails({}));
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
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    if (values.totalPrice !== undefined && typeof values.totalPrice !== 'number') {
      values.totalPrice = Number(values.totalPrice);
    }

    const entity = {
      ...orderProductEntity,
      ...values,
      order: orders.find(it => it.id.toString() === values.order?.toString()),
      productDetails: productDetails.find(it => it.id.toString() === values.productDetails?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...orderProductEntity,
          order: orderProductEntity?.order?.id,
          productDetails: orderProductEntity?.productDetails?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissAsstApp.orderProduct.home.createOrEditLabel" data-cy="OrderProductCreateUpdateHeading">
            <Translate contentKey="larissAsstApp.orderProduct.home.createOrEditLabel">Create or edit a OrderProduct</Translate>
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
                  id="order-product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissAsstApp.orderProduct.quantity')}
                id="order-product-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('larissAsstApp.orderProduct.totalPrice')}
                id="order-product-totalPrice"
                name="totalPrice"
                data-cy="totalPrice"
                type="text"
              />
              <ValidatedField
                id="order-product-order"
                name="order"
                data-cy="order"
                label={translate('larissAsstApp.orderProduct.order')}
                type="select"
              >
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-product-productDetails"
                name="productDetails"
                data-cy="productDetails"
                label={translate('larissAsstApp.orderProduct.productDetails')}
                type="select"
              >
                <option value="" key="0" />
                {productDetails
                  ? productDetails.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order-product" replace color="info">
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

export default OrderProductUpdate;
